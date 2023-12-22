package practice.lab02.graphsAPI;

import java.util.List;

import practice.lab02.graphsAPI.model.LinkItem;
import practice.lab02.graphsAPI.model.NodeItem;

public interface OnPointXYChangedListener {

    void onXYChanged(NodeItem newNode, List<LinkItem> newLinks);
}