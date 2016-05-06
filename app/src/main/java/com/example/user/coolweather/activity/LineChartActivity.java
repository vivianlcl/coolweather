package com.example.user.coolweather.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.example.user.coolweather.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by user on 16-5-6.
 */
public class LineChartActivity extends Activity {
    private LineChartView lineChartView;
    private LineChartData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linear_chart);

        lineChartView = (LineChartView)findViewById(R.id.line_chart_view);
        List<Line> lines = initLine();
        data = initData(lines);

        lineChartView.setLineChartData(data);

        Viewport viewport = initViewPort();
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);

    }
    private Viewport initViewPort(){
        Viewport viewport = new Viewport();
        viewport.top = 100;
        viewport.bottom = 0;
        viewport.left = 0;
        viewport.right = 10;

        return viewport;
    }

    private List<Line> initLine(){
        List<Line> lineList = new ArrayList<Line>();
        List<PointValue> pointValueList = new ArrayList<PointValue>();

        PointValue pointValue1 = new PointValue(10,30);
        pointValueList.add(pointValue1);

        PointValue pointValue2 = new PointValue(10,30);
        pointValueList.add(pointValue2);

        PointValue pointValue3 = new PointValue(10,30);
        pointValueList.add(pointValue3);

        PointValue pointValue4 = new PointValue(10,30);
        pointValueList.add(pointValue4);

        PointValue pointValue5 = new PointValue(10,30);
        pointValueList.add(pointValue5);

        PointValue pointValue6 = new PointValue(10,30);
        pointValueList.add(pointValue6);

        PointValue pointValue7 = new PointValue(10,30);
        pointValueList.add(pointValue7);

        PointValue pointValue8 = new PointValue(10,30);
        pointValueList.add(pointValue8);

        Line line = new Line(pointValueList);
        line.setColor(Color.BLUE);
        line.setShape(ValueShape.CIRCLE);
        lineList.add(line);

        return lineList;
    }

    private LineChartData initData(List<Line> lines){
        LineChartData data = new LineChartData();

        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("时间");
        axisY.setName("销量");

        data.setAxisYLeft(axisX);
        data.setAxisXBottom(axisX);

        return data;
    }
}
