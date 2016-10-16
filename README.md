# MVP_Retrofit
MVP设计模式在Android中的应用

# MVC_Retrofit_Android
MVC设计模式和Retrofit框架在Android项目中的应用

#先看项目演示
![image](https://github.com/fanjianli/YanShi/blob/master/MVC_yanshi.png)

##
#MVP

	视图（View）：也就是Android中的Activity。同时需要创建一个View的抽象接口View interface。需要View实现的接口，View通过View interface与Presenter进行交互，降低耦合。。
	纽带（Persenter）：作为View与Model交互的中间纽带，处理与用户交互的负责逻辑。(抽象接口出来方便通信)
	模型（Model）：用来操做实际数据（譬如数据存储等）。有时也需要创建一个Model的抽象接口Model interface用来降低耦合
如下图很直观的展示了MVP框架的核心：

![image](https://github.com/fanjianli/YanShi/blob/master/MVP_.png)
	

	View实例化Persenter抽象类,通过Persenter控制Model获取数据，Model获取了数据回调Persenter改变View。简单来说就是在MVC的基础上把VC都当作了V，通过中间纽带Persenter连接V和M。

##
#应用
  架构图：
![image](https://github.com/fanjianli/YanShi/blob/master/mvp.png)

View层：MainActivity实现View接口方便实例化时交给Persenter管理，通过Persenter回调，activity中只做UI操作。


	public class MainActivity extends AppCompatActivity implements MainView{

    @Bind(R.id.edit_num)
    EditText editNum;
    @Bind(R.id.btn_go)
    Button btnGo;
    @Bind(R.id.loadding)
    LoadingView loadding;
    @Bind(R.id.show_text)
    TextView showText;
    private MainPresenterImpl presenter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (null == presenter) {
            presenter = new MainPresenterImpl(this);
        }
        presenter.initView();
    }
    @Override
    public void initView() {
        loadding.withLoadedEmptyText("≥﹏≤ , 连条毛都没有 !").withEmptyIco(R.mipmap.disk_file_no_data).withBtnEmptyEnnable(false)
                .withErrorIco(R.mipmap.ic_chat_empty).withLoadedErrorText("(῀( ˙᷄ỏ˙᷅ )῀)ᵒᵐᵍᵎᵎᵎ,我家程序猿跑路了 !").withbtnErrorText("去找回她!!!")
                .withLoadedNoNetText("你挡着信号啦o(￣ヘ￣o)☞ᗒᗒ 你走").withNoNetIco(R.mipmap.ic_chat_empty).withbtnNoNetText("网弄好了，重试")
                .withLoadingIco(R.drawable.loading_animation).withLoadingText("加载中...").withOnRetryListener(new OnRetryListener() {
            @Override
            public void onRetry(){
                //传送给P层次
                presenter.sendRequest(MainActivity.this,editNum.getText().toString().trim());
            }
        }).build();
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //传送给P层次
                presenter.sendRequest(MainActivity.this,editNum.getText().toString().trim());
            }
        });
        loadding.setVisibility(View.GONE);
        showText.setVisibility(View.GONE);
    }

    @Override
    public void showSuccess(Weather weather) {
        showText.setText(weather.toString());
        loadding.setVisibility(View.GONE);
        showText.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoNet() {
        loadding.setState(LoadingState.STATE_NO_NET);
        loadding.setVisibility(View.VISIBLE);
        showText.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        loadding.setState(LoadingState.STATE_ERROR);
        loadding.setVisibility(View.VISIBLE);
        showText.setVisibility(View.GONE);
    }

    @Override
    public void showLoadding() {
        loadding.setState(LoadingState.STATE_LOADING);
        loadding.setVisibility(View.VISIBLE);
        showText.setVisibility(View.GONE);
    }
	}

Model层做一些获取数据或者改变数据的操作，可以通过传递Persenter接口实现回掉，也可以直接实例化Persenter实现回调（接口比较直观）：

	public class MainModelImpl implements MainModle{
    private static final String TAG = "MainModelImpl";
    /**
     * 实现MainModole获取天气信息
     * Created by fan on 2016/10/16.
     * cityNum:城市代码
     * listener:网络连接是否成功接口
     */
    @Override
    public void getWeather(Context context, String cityNum, final MainPresenter listener) {
        ApiManager.getWeather(cityNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Weather>() {
            @Override
            public void call(Weather weather) {
                Log.e(TAG, "MainModelImpl:" + weather);
                listener.onSuccess(weather);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG, "MainModelImpl:" + throwable);
                listener.onError();
            }
        });
    }
	}


Persenter层：控制View和Modle

	public class MainPresenterImpl implements MainPresenter{
    private MainView mainView;
    private MainModle mainModle;
    public MainPresenterImpl(MainView mainView){
        this.mainView = mainView;
        mainModle = new MainModelImpl();
    }

    @Override
    public void onSuccess(Weather weather) {
        mainView.showSuccess(weather);
    }

    @Override
    public void onError() {
        mainView.showError();
    }

    @Override
    public void showNoNet() {
        mainView.showNoNet();
    }

    @Override
    public void initView() {
        mainView.initView();
    }

    @Override
    public void sendRequest(Context context, String cityNum) {
        mainView.showLoadding();
        mainModle.getWeather(context,cityNum,this);
    }

#主要代码
*MainActivity:


    private MainPresenter presenter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (null == presenter) {
            presenter = new MainPresenterImpl(this);//实例化Persenter
        }
        presenter.initView();
    }
*Presenter:
	
	private MainView mainView;
    private MainModle mainModle;
    public MainPresenterImpl(MainView mainView){
        this.mainView = mainView;
        mainModle = new MainModelImpl();
    }

*Model:
 	通过接口连接M和View

    public void getWeather(Context context, String cityNum, final MainPresenter listener) {
        ApiManager.getWeather(cityNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Weather>() {
            @Override
            public void call(Weather weather) {
                Log.e(TAG, "MainModelImpl:" + weather);
                listener.onSuccess(weather);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG, "MainModelImpl:" + throwable);
                listener.onError();
            }
        });
    }

#Retrofit2

  	private static final String STARTURL = "http://apis.baidu.com";
    private static final Retrofit getServiceList = new Retrofit.Builder()
            .baseUrl(STARTURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 使用RxJava作为回调适配器
            .build();
   	private static final ApiService apiManager = getServiceList.create(ApiService.class);
    public static Observable<Weather> getWeather(String pinyin){
        return apiManager.getWeather("c50757d2e4d31f64290d503c74fe6054",pinyin);
    }


	public interface ApiService {


    @GET("/apistore/weatherservice/weather")
    Observable<Weather> getWeather(@Header("apikey") String apikey, @Query("citypinyin") String pinyin);
	}
