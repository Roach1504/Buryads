package uk.co.ribot.androidboilerplate.ui.main;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.model.RegistRespons;
import uk.co.ribot.androidboilerplate.data.model.Ribot;
import uk.co.ribot.androidboilerplate.data.model.Talk;
import uk.co.ribot.androidboilerplate.injection.ConfigPersistent;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;
import uk.co.ribot.androidboilerplate.ui.login.LoginPresenter;
import uk.co.ribot.androidboilerplate.util.RxUtil;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public MainPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void loadRibots() {
        checkViewAttached();
        RxUtil.unsubscribe(mSubscription);
        mSubscription = mDataManager.getRibots()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Ribot>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the ribots.");
                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(List<Ribot> ribots) {
                        if (ribots.isEmpty()) {
                            getMvpView().showRibotsEmpty();
                        } else {
                            getMvpView().showRibots(ribots);
                            loadTest("LoginTest", "PassTest","NameTest", "FamilyTest", "CityTest", "00000000");
                        }
                    }
                });
    }
    public void loadTest(String login, String pass, String name
            , String family
            , String city
            , String tel){
        RxUtil.unsubscribe(mSubscription);
        mSubscription = mDataManager.getRegist(login, pass, name
                , family
                , city
                , tel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<RegistRespons>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("error",e.getMessage()+" ||| ");
                        Timber.e(e, "There was an error loading the ribots.");

                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(RegistRespons respons) {
                        if (respons.getId().isEmpty()) {
                            Log.e("TEST", "0000");

                        } else {
                            Log.e("TEST", "id = "+respons.getId() + " status: "+ respons.getStatus());
                        }
                    }
                });

    }

}
