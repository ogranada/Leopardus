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


Simple Tutorial
---------------

The Base activiti classes are BaseFragmentsActivity and BaseDrawerFragmentsActivity, you can Inject views and methods and menu items.

```

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

}

```

