package co.ritzonex.uqude;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import co.ritzonex.uqude.dummy.DummyContent;
import co.ritzonex.uqude.tool.MyTool;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;
import com.squareup.picasso.Picasso;

public class AnswerActivity extends MyActivity {
	public static Pattern patternQuestionTitle = Pattern
			.compile("(?s)question-title\"><h3>(.*?)</h3>");
	public static Pattern patternImage = Pattern
			.compile("(?s)class=\"take-question.*?src=\"(.*?)\"");
	public static Pattern patternResult = Pattern.compile(">提交<");

	private TextView title;
	private ImageView image;
	private TextView questionTip;
	private TextView questionTitle;

	private LinearLayout layout;
	private String data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer);

		layout = (LinearLayout) findViewById(R.id.layout);
		title = (TextView) findViewById(R.id.title);
		image = (ImageView) findViewById(R.id.image);
		questionTip = (TextView) findViewById(R.id.question_tip);
		questionTitle = (TextView) findViewById(R.id.question_title);

		String url = getIntent().getStringExtra(QuizActivity.URL);
		MyTool.get(this, url, responseHandler);
		// new GetDataTask().execute();
	}

	private String getImageUrl() {
		Matcher matcher = patternImage.matcher(data);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	private void addButtons() {
		Pattern pattern = Pattern.compile("(?s)<td(.*?)</td>");
		// 定义一个matcher用来做匹配
		Matcher matcher = pattern.matcher(data);

		while (matcher.find()) {
			ButtonItem item = new ButtonItem(matcher.group(1));
			Button button = new Button(AnswerActivity.this);
			button.setText(item.text);
			button.setTag(item);
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(AnswerActivity.this,
							resultClass());
					ButtonItem item = (ButtonItem) v.getTag();
					intent.putExtra(QuizActivity.URL, item.url);
					startActivity(intent);
				}

				private Class<?> resultClass() {
					Matcher matcher = patternResult.matcher(data);
					if (matcher.find()) {
						return ResultActivity.class;
					} else
						return AnswerActivity.class;
				}
			});
			layout.addView(button);
		}
	}

	private CharSequence getQuestionTitle() {
		Matcher matcher = patternQuestionTitle.matcher(data);
		if (matcher.find())
			return Html.fromHtml(matcher.group(1));
		return null;
	}

	private CharSequence getQuestionTip() {
		Matcher matcher = QuizActivity.patternQuestionTip.matcher(data);
		if (matcher.find())
			return Html.fromHtml(matcher.group(1));
		return null;
	}

	private CharSequence getAnswerTitle() {
		Matcher matcher = QuizActivity.patternTitle.matcher(data);
		if (matcher.find())
			return Html.fromHtml(matcher.group(1));
		return null;
	}

	static class ButtonItem {
		public static Pattern patternText = Pattern.compile("(?s)/>(.*?)</p>");
		Spanned text;
		String url;

		public ButtonItem(String input) {
			Matcher matcher = patternText.matcher(input);
			if (matcher.find())
				text = Html.fromHtml(matcher.group(1).trim());

			String id = find(input, Pattern.compile("id=\"(.*?)\""));
			String xiaoti = find(input, Pattern.compile("name=\"(.*?)\""));
			String daan = find(input, Pattern.compile("value=\"(.*?)\""));

			url = DummyContent.UQUDE_URL + "/quiz/answer/" + id + "/" + xiaoti
					+ "/" + daan;
		}

		private String find(String input, Pattern pattern) {
			Matcher matcher = pattern.matcher(input);
			if (matcher.find())
				return matcher.group(1);
			return null;
		}

	}

	private ResponseHandlerInterface responseHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onStart() {
			// called before request is started
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] response) {
			// called when response HTTP status is "200 OK"
			data = new String(response);
			title.setText(getAnswerTitle());
			questionTip.setText(getQuestionTip());
			questionTitle.setText(getQuestionTitle());
			Picasso.with(AnswerActivity.this).load(getImageUrl())
					.placeholder(R.drawable.placeholder)
					.error(R.drawable.error).into(image);
			addButtons();
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] errorResponse, Throwable e) {
			// called when response HTTP status is "4XX" (eg. 401, 403, 404)
			Toast.makeText(AnswerActivity.this, "加载失败", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onRetry(int retryNo) {
			// called when request is retried
			Toast.makeText(AnswerActivity.this, "onRetry", Toast.LENGTH_SHORT)
					.show();
		}
	};

}
