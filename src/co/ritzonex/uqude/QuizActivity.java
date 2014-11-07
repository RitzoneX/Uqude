package co.ritzonex.uqude;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import co.ritzonex.uqude.tool.MyTool;

public class QuizActivity extends MyActivity {

	public static final String URL = "url";
	private String data;
	private TextView title;
	private TextView description;
	private TextView questionTip;
	private Button btnTest;

	public static Pattern patternTitle = Pattern
			.compile("(?s)class=\"icons icons2\".*?<h1>(.*?)</h1>");
	public static Pattern patternQuestionTip = Pattern
			.compile("(?s)class=\"f20\">(.*?)</p>");
	public static Pattern patternDescription = Pattern
			.compile("(?s)class=\"f14 pt-12\">(.*?)</p>");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);

		title = (TextView) findViewById(R.id.title);
		description = (TextView) findViewById(R.id.description);
		questionTip = (TextView) findViewById(R.id.question_tip);
		btnTest = (Button) findViewById(R.id.btn_test);

		new GetDataTask().execute();
	}

	private CharSequence getQuizQuestionTip() {
		Matcher matcher = patternQuestionTip.matcher(data);
		if (matcher.find())
			return Html.fromHtml(matcher.group(1));
		return null;
	}

	private CharSequence getQuizDescription() {
		Matcher matcher = patternDescription.matcher(data);
		Spanned temp;
		if (matcher.find()) {
			temp = Html.fromHtml(matcher.group(1));
			return Html.fromHtml(temp.toString());
		}
		return null;
	}

	private CharSequence getQuizTitle() {
		Matcher matcher = patternTitle.matcher(data);
		if (matcher.find())
			return Html.fromHtml(matcher.group(1));
		return null;
	}

	private class GetDataTask extends AsyncTask<Void, Void, String> {

		private String url;

		@Override
		protected String doInBackground(Void... params) {
			url = getIntent().getStringExtra(URL);
			return MyTool.getUrlData(url);
		}

		@Override
		protected void onPostExecute(String result) {
			if (result == null) {
				Toast.makeText(QuizActivity.this, "加载失败", Toast.LENGTH_LONG)
						.show();
				return;
			}
			data = result;
			title.setText(getQuizTitle());
			description.setText(getQuizDescription());
			questionTip.setText(getQuizQuestionTip());
			btnTest.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(QuizActivity.this,
							AnswerActivity.class);
					intent.putExtra(URL, url + "/start");
					startActivity(intent);
				}
			});
		}

	}
}
