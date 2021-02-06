package com.lemoncode.relations;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class ConnectionsDTO {

    private String status;
    private List<Node> nodes;
    private List<Edge> links;
    private String relationLabel;

    public static ConnectionsDTO noLink() {
        ConnectionsDTO dto = new ConnectionsDTO();
        dto.setStatus("success");
        dto.setRelationLabel("noLink");
        return dto;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class Node {
        private String id;
        private String label;

        public void setId(Long id){
            this.id = String.valueOf(id);
        }

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Edge {
        private String id;
        private String source;
        private String target;
        private String label;

        public void setSource(Long source){
            this.source = String.valueOf(source);
        }

        public void setTarget(Long target){
            this.target = String.valueOf(target);
        }

        public void setId(int id){
        //    String s = Character.toString((char) (97 +id));
            this.id = "a" + id;
        }

    }
}
