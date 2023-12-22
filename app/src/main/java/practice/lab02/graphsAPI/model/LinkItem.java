package practice.lab02.graphsAPI.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class LinkItem {
    @SerializedName("id")
    public Integer id;
    @SerializedName("source")
    public Integer sourceNodeId;
    @SerializedName("target")
    public Integer targetNodeId;
    @SerializedName("value")
    public Float value;

    public LinkItem(Integer id, Integer sourceNodeId, Integer targetNodeId, Float value) {
        this.id = id;
        this.sourceNodeId = sourceNodeId;
        this.targetNodeId = targetNodeId;
        this.value = value;
    }

    public void drawLink(Canvas canvas, NodeItem sourceNode, NodeItem targetNode) {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(3);
        canvas.drawLine(sourceNode.x, sourceNode.y, targetNode.x, targetNode.y, paint);
        fillArrow(paint, canvas, sourceNode.x, sourceNode.y, targetNode.x, targetNode.y);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(35);
        canvas.drawText(value.toString(), (targetNode.x + ((targetNode.x + sourceNode.x) / 2)) / 2, (targetNode.y + ((targetNode.y + sourceNode.y) / 2)) / 2, textPaint);
    }

    //перемещение
//    public void translate(Integer nodeId, Float newX, Float newY) {
//        if (Objects.equals(nodeId, firstNodeId)) {
//            this.firstX += newX;
//            this.firstY += newY;
//        } else {
//            this.secondX += newX;
//            this.secondY += newY;
//        }
//    }

    private void fillArrow(Paint paint, Canvas canvas, float x0, float y0, float x1, float y1) {
        paint.setStyle(Paint.Style.FILL);

        float deltaX = x1 - x0;
        float deltaY = y1 - y0;
        double distance = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
        float frac = (float) (1 / (distance / 20));

        float point_x_1 = x0 + (float) ((1 - frac) * deltaX + frac * deltaY);
        float point_y_1 = y0 + (float) ((1 - frac) * deltaY - frac * deltaX);

        float point_x_2 = x1;
        float point_y_2 = y1;

        float point_x_3 = x0 + (float) ((1 - frac) * deltaX - frac * deltaY);
        float point_y_3 = y0 + (float) ((1 - frac) * deltaY + frac * deltaX);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);

        path.moveTo(point_x_1, point_y_1);
        path.lineTo(point_x_2, point_y_2);
        path.lineTo(point_x_3, point_y_3);
        path.lineTo(point_x_1, point_y_1);
        path.lineTo(point_x_1, point_y_1);
        path.close();

        canvas.drawPath(path, paint);
    }
}
