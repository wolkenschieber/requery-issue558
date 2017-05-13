# requery-issue558

This is a scenario for [Requery issue #558](https://github.com/requery/requery/issues/558). It demonstrates the behaviour of different transaction modes set in the datastore.

## Results

### No transactions

Queries without transactions perform fine.

### `TransactionMode.NONE`

`TransactionMode.NONE` crashes the application with `io.reactivex.exceptions.OnErrorNotImplementedException`/`java.lang.IllegalStateException`.

### `TransactionMode.MANAGED`

`TransactionMode.MANAGED` crashes the application with `java.lang.NoClassDefFoundError: Failed resolution of: Ljavax/transaction/Synchronization`

### `TransactionMode.AUTO`

Queries within transactions perform fine.

However the application logs a `NoClassDefFoundError` on info level:

```
 Rejecting re-init on previously-failed class java.lang.Class<io.requery.sql.ManagedTransaction>: java.lang.NoClassDefFoundError: Failed resolution of: Ljavax/transaction/Synchronization;
     at void io.requery.sql.TransactionProvider.<init>(io.requery.sql.RuntimeConfiguration) (TransactionProvider.java:26)
     at void io.requery.sql.EntityDataStore.<init>(io.requery.sql.Configuration) (EntityDataStore.java:159)
     at void de.nebelhoernchen.requery558.App.setTransactionMode(io.requery.sql.TransactionMode) (App.java:42)
     at io.requery.reactivex.ReactiveEntityStore de.nebelhoernchen.requery558.MainActivity.getData(io.requery.sql.TransactionMode) (MainActivity.java:65)
     at void de.nebelhoernchen.requery558.MainActivity.btnOnClick(android.view.View, io.requery.sql.TransactionMode) (MainActivity.java:59)
     at void de.nebelhoernchen.requery558.MainActivity.btnOnClickTransactionAuto(android.view.View) (MainActivity.java:54)

 Caused by: java.lang.ClassNotFoundException: Didn't find class "javax.transaction.Synchronization" on path: DexPathList[[zip file "/data/app/de.nebelhoernchen.requery558-1/base.apk", zip file
"/data/app/de.nebelhoernchen.requery558-1/split_lib_dependencies_apk.apk", zip file "/data/app/de.nebelhoernchen.requery558-1/split_lib_slice_0_apk.apk", zip file "/data/app/de.nebelhoernchen.requery558-1/split_lib_slice_1_apk.apk", zip file
"/data/app/de.nebelhoernchen.requery558-1/split_lib_slice_2_apk.apk", zip file "/data/app/de.nebelhoernchen.requery558-1/split_lib_slice_3_apk.apk", zip file "/data/app/de.nebelhoernchen.requery558-1/split_lib_slice_4_apk.apk", zip file
"/data/app/de.nebelhoernchen.requery558-1/split_lib_slice_5_apk.apk", zip file "/data/app/de.nebelhoernchen.requery558-1/split_lib_slice_6_apk.apk", zip file "/data/app/de.nebelhoernchen.requery558-1/split_lib_slice_7_apk.apk", zip file
"/data/app/de.nebelhoernchen.requery558-1/split_lib_slice_8_apk.apk", zip file "/data/app/de.nebelhoernchen.requery
     at java.lang.Class dalvik.system.BaseDexClassLoader.findClass(java.lang.String) (BaseDexClassLoader.java:56)
     at java.lang.Class java.lang.ClassLoader.loadClass(java.lang.String, boolean) (ClassLoader.java:380)
     at java.lang.Class java.lang.ClassLoader.loadClass(java.lang.String) (ClassLoader.java:312)
     at void io.requery.sql.TransactionProvider.<init>(io.requery.sql.RuntimeConfiguration) (TransactionProvider.java:26)
     at void io.requery.sql.EntityDataStore.<init>(io.requery.sql.Configuration) (EntityDataStore.java:159)   
     at void de.nebelhoernchen.requery558.App.setTransactionMode(io.requery.sql.TransactionMode) (App.java:42)
```

