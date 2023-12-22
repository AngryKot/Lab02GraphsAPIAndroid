package practice.lab02.graphsAPI.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class GraphItem {
    @SerializedName("id")
    public Integer id;
    @SerializedName("name")
    public String name;
    @SerializedName("timestamp")
    public Integer timestamp;
    @SerializedName("nodes")
    public Integer nodes;


    public GraphItem(Integer id, String name, Integer timestamp, Integer nodes) {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
        this.nodes = nodes;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public Integer getNodes() {
        return nodes;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
