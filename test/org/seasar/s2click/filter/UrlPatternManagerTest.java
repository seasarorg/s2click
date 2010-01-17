/*
 * Copyright 2006-2010 the Seasar Foundation and the Others.
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
package org.seasar.s2click.filter;

import java.util.List;

import junit.framework.TestCase;

import org.seasar.s2click.filter.UrlPatternManager.UrlRewriteInfo;

public class UrlPatternManagerTest extends TestCase {

	public void testUrlPatternManager() {
		try {
			UrlPatternManager.add("/article/{articleId}", "/article.htm");
			UrlPatternManager.add("/article/{categoryId}/{articleId}", "/category.htm");
			
			List<UrlRewriteInfo> list = UrlPatternManager.getAll();
			assertEquals(2, list.size());
			
			UrlRewriteInfo info1 = list.get(0);
			assertEquals("/article.htm", info1.realPath);
			assertEquals(1, info1.parameters.length);
			assertEquals("articleId", info1.parameters[0]);
			assertEquals("\\Q/article/\\E(.+?)\\Q\\E", info1.pattern.toString());
			
			UrlRewriteInfo info2 = list.get(1);
			assertEquals("/category.htm", info2.realPath);
			assertEquals(2, info2.parameters.length);
			assertEquals("categoryId", info2.parameters[0]);
			assertEquals("articleId", info2.parameters[1]);
			assertEquals("\\Q/article/\\E(.+?)\\Q/\\E(.+?)\\Q\\E", info2.pattern.toString());
			
		} finally {
			UrlPatternManager.getAll().clear();
		}
	}

}
