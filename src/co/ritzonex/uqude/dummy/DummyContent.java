package co.ritzonex.uqude.dummy;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.Html;
import android.text.Spanned;
import co.ritzonex.uqude.tool.MyTool;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

	/**
	 * An array of sample (dummy) items.
	 */
	public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	// public static Map<String, DummyItem> ITEM_MAP = new HashMap<String,
	// DummyItem>();

	private static final String URL = "http://www.uqude.com/hot/";
	public static final String UQUDE_URL = "http://www.uqude.com";
//	private static int page = 500;

	// static {
	// // Add 3 sample items.
	// addItem(new DummyItem("1", "Item 1"));
	// addItem(new DummyItem("2", "Item 2"));
	// addItem(new DummyItem("3", "Item 3"));
	// }

	// 连接网络更新数据
	public static void update() {
		handle(MyTool.getUrlData(URL + page()));
	}
	
	private static int page() {
		return (int) (Math.random() * 500) + 1;
	}

	private static void handle(String html) {
		Pattern pattern = Pattern.compile("<!-- 测试(.*?)</li>");
		// 定义一个matcher用来做匹配
		Matcher matcher = pattern.matcher(html);

		while (matcher.find()) {
			addItem(new DummyItem(matcher.group(1)));
		}
	}

	private static void addItem(DummyItem item) {
		ITEMS.add(item);
		// ITEM_MAP.put(item.id, item);
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class DummyItem {
		public String title;
		public Spanned content;
		public String imageUrl;
		public String url;
		private static Pattern patternTitle = Pattern
				.compile("class=\"pb-10.*?>([^>]*?)</a>");
		private static Pattern patternContent = Pattern
				.compile("class=\"lineH22\">(.*?)</p>");
		private static Pattern patternImageUrl = Pattern
				.compile("src=\"(.*?)!");
		private static Pattern patternUrl = Pattern.compile("href=\"(.*?)\"");

		public DummyItem(String input) {
			Matcher matcher = patternTitle.matcher(input);
			if (matcher.find())
				this.title = matcher.group(1);

			matcher = patternContent.matcher(input);
			if (matcher.find())
				this.content = Html.fromHtml(matcher.group(1).trim());

			matcher = patternImageUrl.matcher(input);
			if (matcher.find())
				imageUrl = matcher.group(1);

			matcher = patternUrl.matcher(input);
			if (matcher.find())
				url = UQUDE_URL + matcher.group(1);
		}

		public String getTitle() {
			return title;
		}

		public Spanned getContent() {
			return content;
		}

		public String getImageUrl() {
			return imageUrl;
		}

		public String getUrl() {
			return url;
		}

		@Override
		public String toString() {
			return "DummyItem [title=" + title + ", content=" + content
					+ ", imageUrl=" + imageUrl + ", url=" + url + "]";
		}

	}
}
