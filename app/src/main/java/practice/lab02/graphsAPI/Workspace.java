package practice.lab02.graphsAPI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

import practice.lab02.graphsAPI.model.LinkItem;
import practice.lab02.graphsAPI.model.NodeItem;

public class Workspace extends SurfaceView {
    private OnPointXYChangedListener listener;
    private OnPointClickListener clickListener;
    private ArrayList<NodeItem> nodes = new ArrayList<>();
    private ArrayList<LinkItem> links = new ArrayList<>();
    NodeItem selected = null;
    Paint p = new Paint();
    float oldX = 0.0f;
    float oldY = 0.0f;

    //  конструктор
    public Workspace (Context context, AttributeSet attrs) {
        super (context, attrs);
        setWillNotDraw(false);
    }

    //для передачи действий со 2й активити
    public void addListeners(OnPointXYChangedListener listener, OnPointClickListener clickListener) {
        this.listener = listener;
        this.clickListener = clickListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float dx = x - oldX;
        float dy = y - oldY;
        oldX = x;
        oldY = y;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                selected = null;
                for (int i = nodes.size() - 1; i >= 0; i--)
                    if(nodes.get(i).point_inside(x, y)) {
                        selected = nodes.get(i);
                        break;
                    }
                invalidate();
                return true;

            case MotionEvent.ACTION_UP:
                if (selected != null) {
                    ArrayList<LinkItem> modifiedLinks = new ArrayList<>();
                    for (LinkItem link : links) {
                        if (link.sourceNodeId == selected.id || link.targetNodeId == selected.id) {
                            modifiedLinks.add(link);
                        }
                    }
                    listener.onXYChanged(selected, modifiedLinks);
                    clickListener.onClick(selected);
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                if(selected != null) {
                    nodes.get(nodes.indexOf(selected)).translate(dx, dy);
                }
                invalidate();
                return true;
        }
        return  false;
    }

    @Override
    protected void onDraw (Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        for(int i = 0; i < nodes.size(); i++) {
            if (selected == nodes.get(i)) p.setColor(Color.RED);
            else p.setColor(Color.GRAY);
            nodes.get(i).drawNode(canvas, p);
        }
        for (LinkItem link : links) {
            link.drawLink(canvas, findNode(link.sourceNodeId), findNode(link.targetNodeId));
        }
    }

    public void refreshPoints(List<NodeItem> newNodes, List<LinkItem> newLinks) {
        nodes.clear();
        links.clear();
        nodes.addAll(newNodes);
        links.addAll(newLinks);

        invalidate();
    }

    public boolean isLinkExists(NodeItem node1, NodeItem node2) {
        for (LinkItem link: links) {
            if(link.sourceNodeId == node1.id && link.targetNodeId == node2.id) {
                return true;
            }
        }
        return false;
    }

    public LinkItem findLink(NodeItem sourceNode, NodeItem targetNode) {
        for (LinkItem link: links) {
            if(link.sourceNodeId == sourceNode.id && link.targetNodeId == targetNode.id) {
                return link;
            }
        }
        return null;
    }

    private NodeItem findNode(Integer nodeId) {
        for (NodeItem node : nodes) {
            if (node.id == nodeId) return node;
        }

        return null;
    }
}
