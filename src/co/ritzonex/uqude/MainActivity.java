package co.ritzonex.uqude;

import android.content.Intent;
import android.os.Bundle;
import co.ritzonex.b.BannerView;
import co.ritzonex.uqude.ItemFragment.OnFragmentInteractionListener;

public class MainActivity extends MyActivity implements
		OnFragmentInteractionListener {
	private BannerView bannerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		bannerView = (BannerView) findViewById(R.id.banner);
		bannerView.showBanner("80a4aaa86f81453cafcc4e985824b17e", "xiaomi");

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new ItemFragment()).commit();
		}
	}

	@Override
	public void onFragmentInteraction(String url) {
		Intent intent = new Intent(this, QuizActivity.class);
		intent.putExtra(QuizActivity.URL, url);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (bannerView != null) {
			bannerView.finishBanner();
		}

	}

}
