/*
 * Copyright 2006-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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
