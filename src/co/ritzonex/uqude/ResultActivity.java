package co.ritzonex.uqude;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import co.ritzonex.uqude.tool.MyTool;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;

public class ResultActivity extends MyActivity implements OnClickListener {
	public static Pattern patternResult = Pattern
			.compile("(?s)class=\"question-title.*?(测试结果.*?)</span>");
	public static Pattern patternDescription = Pattern
			.compile("(?s)class=\"f14\">(.*?)</p>");

	private TextView title;
	private TextView result;
	private TextView description;
	private Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		title = (TextView) findViewById(R.id.title);
		result = (TextView) findViewById(R.id.result);
		description = (TextView) findViewById(R.id.description);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);

		String url = getIntent().getStringExtra(QuizActivity.URL);
		MyTool.get(this, url, responseHandler);
	}

	private ResponseHandlerInterface responseHandler = new AsyncHttpResponseHandler() {

		private String data;

		@Override
		public void onStart() {
			// called before request is started
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] response) {
			// called when response HTTP status is "200 OK"
			data = new String(response);
			initTitle();
			initResult();
			initDescription();
		}

		private void initDescription() {
			Matcher matcher = patternDescription.matcher(data);
			if (matcher.find())
				description.setText(Html.fromHtml(matcher.group(1)));
		}

		private void initResult() {
			Matcher matcher = patternResult.matcher(data);
			if (matcher.find())
				result.setText(Html.fromHtml(matcher.group(1)));
		}

		private void initTitle() {
			Matcher matcher = QuizActivity.patternTitle.matcher(data);
			if (matcher.find())
				title.setText(Html.fromHtml(matcher.group(1)));
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] errorResponse, Throwable e) {
			// called when response HTTP status is "4XX" (eg. 401, 403, 404)
			Toast.makeText(getApplicationContext(), "加载失败", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onRetry(int retryNo) {
			// called when request is retried
			Toast.makeText(getApplicationContext(), "onRetry",
					Toast.LENGTH_SHORT).show();
		}
	};

	// 返回顶部
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}
}
