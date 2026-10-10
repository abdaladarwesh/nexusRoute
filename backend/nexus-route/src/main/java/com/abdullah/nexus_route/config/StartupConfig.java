package com.abdullah.nexus_route.config;


import com.abdullah.nexus_route.entity.CityEdge;
import com.abdullah.nexus_route.entity.CityNode;
import com.abdullah.nexus_route.service.GraphService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@Slf4j
public class StartupConfig implements CommandLineRunner {
    public static class NodeDto {
       public Long id;
       public double lat;
       public double lon;
    }

    public static class EdgeDto {
       public Long from;
       public Long to;
       public double weight;
    }

    public static class NodeResponseWrapper{
        public List<NodeDto> nodes;
    }

    public static class EdgeResponseWrapper{
        public List<EdgeDto> edges;
    }

    private final GraphService graphService;
    private final com.abdullah.nexus_route.repository.UserRepository userRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @org.springframework.beans.factory.annotation.Value("${application.security.demo.email:demo@nexusroute.com}")
    private String demoEmail;

    @org.springframework.beans.factory.annotation.Value("${application.security.demo.password:Password123!}")
    private String demoPassword;

    @org.springframework.beans.factory.annotation.Value("${application.security.demo.full-name:Demo User}")
    private String demoFullName;

    @Override
    public void run(String... args) throws Exception {
        initDemoUser();

        if (graphService.isDatabaseEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            File edgesFile = new ClassPathResource("static/manhattan_road_network_edges.json").getFile();
            File nodesFile = new ClassPathResource("static/manhattan_road_network_nodes.json").getFile();

            NodeResponseWrapper nodesDtos = objectMapper.readValue(nodesFile, NodeResponseWrapper.class);
            EdgeResponseWrapper edgeDtos = objectMapper.readValue(edgesFile, EdgeResponseWrapper.class);

            if (nodesDtos.nodes == null || edgeDtos.edges == null){
                log.error("nodes are = null");
                System.exit(1);
            }

            List<CityNode> cityNodes = nodesDtos.nodes.stream().map((node) -> CityNode.builder()
                    .id(node.id)
                    .longitude(node.lon)
                    .latitude(node.lat)
                    .build()
            ).toList();

            Map<Long, CityNode> nodeMap = cityNodes.stream()
                    .collect(Collectors.toMap(CityNode::getId, node -> node));

            List<CityEdge> cityEdges = edgeDtos.edges.stream()
                    .map(dto -> CityEdge.builder()
                            .start(nodeMap.get(dto.from))
                            .destination(nodeMap.get(dto.to))
                            .weight(dto.weight)
                            .build())
                    .filter(edge -> edge.getStart() != null && edge.getDestination() != null) // Skip edges with missing nodes
                    .toList();

            graphService.addGraphDataToDatabase(cityNodes, cityEdges);
            graphService.addDataToGraph(cityNodes, cityEdges);
        } else {
            graphService.initGraphFromDatabase();
        }
    }

    private void initDemoUser() {
        String normalizedEmail = demoEmail.trim().toLowerCase();
        if (!userRepository.existsByEmail(normalizedEmail)) {
            com.abdullah.nexus_route.entity.User demoUser = com.abdullah.nexus_route.entity.User.builder()
                    .email(normalizedEmail)
                    .password(passwordEncoder.encode(demoPassword))
                    .fullName(demoFullName)
                    .role(com.abdullah.nexus_route.entity.Role.ROLE_USER)
                    .build();
            userRepository.save(demoUser);
            log.info("\n=======================================================\n" +
                     "  DEMO CREDENTIALS INITIALIZED FOR NEXUS ROUTE:\n" +
                     "  Email:    {}\n" +
                     "  Password: {}\n" +
                     "=======================================================", normalizedEmail, demoPassword);
        } else {
            log.info("Demo user '{}' already initialized.", normalizedEmail);
        }
    }
}
