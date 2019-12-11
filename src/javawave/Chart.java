package javawave;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;
import sun.java2d.loops.DrawLine;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Random;

import static org.jfree.chart.ChartFactory.createPieChart;

public class Chart {

    public static JFreeChart Generate_chart(double[] graph_chart){
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        for(int i = 0; i < graph_chart.length; i++) {
            ds.setValue(graph_chart[i], "",""+ i +"");
        }

        JFreeChart chart = ChartFactory.createBarChart("", "", "", ds, PlotOrientation.VERTICAL,false, false, false);
        chart.setBackgroundPaint(Color.GRAY);
        CategoryPlot categoryPlot = chart.getCategoryPlot();
        categoryPlot.setBackgroundPaint(Color.GRAY);
        // get Y Axis
        NumberAxis rangeAxis = (NumberAxis) categoryPlot.getRangeAxis();
        rangeAxis.setVisible(false);
        // get X Axis
        CategoryAxis categoryAxis = (CategoryAxis) categoryPlot.getDomainAxis();
        categoryAxis.setVisible(false);

        return chart;
    }

    public static void main(String[] args) {

        /*
        double[] graph_chart = new double[40];
        graph_chart = [0.6641414141414141, 0.6616161616161617, 0.6363636363636364, 0.6464646464646465, 0.6540404040404041,
                0.6237373737373737, 0.6944444444444444, 0.7752525252525253, 1.0, 0.851010101010101,
                1.0, 0.9015151515151515, 1.0, 0.9747474747474747, 1.0,
                0.7727272727272727, 0.9974747474747475, 0.9090909090909091, 0.8358585858585859, 0.73989898989899,
                0.7070707070707071, 0.5782828282828283, 0.5757575757575758, 0.7828282828282829, 0.6338383838383839,
                0.6439393939393939, 0.5959595959595959, 0.5580808080808081, 0.6843434343434344, 0.7272727272727273,
                0.76010101010101, 0.6590909090909091, 0.6590909090909091, 0.648989898989899, 0.6414141414141414,
                0.6767676767676768, 0.7045454545454546, 0.6742424242424242, 0.6540404040404041, 0.6616161616161617];

         */

        Random r = new Random();
        double[] graph_chart = new double[40];
        for(int i = 0; i < graph_chart.length; i++) {
            graph_chart[i] = r.nextDouble();
            System.out.print(graph_chart[i] + ",");
        }

        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        for(int i = 0; i < graph_chart.length; i++) {
            ds.setValue(graph_chart[i], "",""+ i +"");
        }

        String filePath = "ggg.jpg"; //按照你需要的地点来放
        createChart(ds,filePath);
    }



    public static void createChart(DefaultCategoryDataset ds, String filePath) {
        try {
            // create chart                 在这里把createBarChart函数改成别的种类就可以生成不同的chart了
            JFreeChart chart = ChartFactory.createBarChart("", "", "", ds, PlotOrientation.VERTICAL,false, false, false);
            chart.setBackgroundPaint(Color.GRAY);
            CategoryPlot categoryPlot = chart.getCategoryPlot();
            categoryPlot.setBackgroundPaint(Color.GRAY);
            // get Y Axis
            NumberAxis rangeAxis = (NumberAxis) categoryPlot.getRangeAxis();
            rangeAxis.setVisible(false);
            // get X Axis
            CategoryAxis categoryAxis = (CategoryAxis) categoryPlot.getDomainAxis();
            categoryAxis.setVisible(false);

            ChartUtilities.saveChartAsJPEG(new File(filePath), chart, 400, 100);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
