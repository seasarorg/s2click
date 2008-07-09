package org.seasar.s2click.example.page;

import net.sf.click.extras.graph.JSBarChart;

/**
 * グラフコントロールのサンプルページ。
 * 
 * @author Naoki Takezoe
 */
public class GraphPage extends LayoutPage {
	
	public String title = "JSGraphicsによるグラフチャート";
	public JSBarChart barChart = new JSBarChart("barChart", "棒グラフ");
	
	public GraphPage() {
		super.onInit();

		barChart.setChartHeight(300);
		barChart.setChartWidth(400);

		barChart.addPoint("1", new Integer(145));
		barChart.addPoint("2", new Integer(0));
		barChart.addPoint("3", new Integer(175));
		barChart.addPoint("4", new Integer(130));
		barChart.addPoint("5", new Integer(150));
		barChart.addPoint("6", new Integer(175));
		barChart.addPoint("7", new Integer(205));
		barChart.addPoint("8", new Integer(125));
		barChart.addPoint("9", new Integer(125));
		barChart.addPoint("10", new Integer(135));
		barChart.addPoint("11", new Integer(125));
	}


}
