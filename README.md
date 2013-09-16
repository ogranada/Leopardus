Leopardus
=========

Yet another android framework
-----------------------------
This framework includes:
* DI: Views and events Injection into fragments, fragment activities and MenuItems.
* ActionBarSherlock.
* SlidingMenu.
* MenuDrawer.
* REST cacheable support.
* Image download and caching.
* PullToRefresh
* Fading Action Bar

TODO:
* Injectable Model based ListViews


Simple Tutorial
---------------

The Base activity classes are BaseFragmentsActivity and BaseDrawerFragmentsActivity, you can Inject views, methods and menu items.

```java

class MyFragmentActivity extends BaseFragmentsActivity{
  
    // Inject R.id.btn into btnA
    @InjectView(id=R.id.btn)
    Button btnA;
    ...
    
    // Inject myLongClick into view identified by R.id.btn2
    @InjectMethod(id=R.id.btn2, method=InjectableMethods.OnLongClickListener)
    public void myLongClick(View v){
        ...
    }
    ...
    
    // Inject item into Menu and execute specified method. 
    InjectMenuItem(stringId = R.string.another, iconId = R.drawable.ic_launcher_another)
    public void onAnotherItemClick(ListView lv, View v, long id) {
        ...
    }
    

}

```

The avaliable fragment classes are BaseFragment (usable with BaseFragmentsActivity) and BaseFragmentDrawer (usable with BaseDrawerFragmentsActivity)

```java

public class ListaFragment extends BaseFragment {
    
    @InjectView(id=R.id.btntest)
    Button b;

    @InjectView(id=R.id.imageView1)
    ImageView imagen;
      
      
    public ListaFragment() {
        // The layout can be specified in the constructor
        super(R.layout.activity_lista_fragment);
        // enable fading action bar
        setFadingActionBarEnabled(true);
        /*
        the fading action bar need AppTheme.TranslucentActionBar theme in the manifest

        android:theme="@style/AppTheme.TranslucentActionBar"

        */
    }


    @InjectMethod(id=R.id.btntest)
    public void onBtn1Click(View v){
        this.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), "Click", Toast.LENGTH_LONG).show();
            }
        });
    }
    
}


```
