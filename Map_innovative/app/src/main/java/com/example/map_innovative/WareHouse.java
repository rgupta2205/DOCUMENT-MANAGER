package com.example.map_innovative;

public class WareHouse {
    public String name,node,type,uid;

    public WareHouse(String name, String node, String type, String uid) {
        this.name = name;
        this.node = node;
        this.type = type;
        this.uid = uid;
    }

    public WareHouse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
