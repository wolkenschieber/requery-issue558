package de.nebelhoernchen.requery558;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import de.nebelhoernchen.requery558.model.ChildEntity;
import de.nebelhoernchen.requery558.model.ParentEntity;
import io.reactivex.Completable;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.sql.TransactionMode;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnOnClickTransactionNone(View view)
    {
        btnOnClick(view, TransactionMode.NONE);
    }

    public void btnOnClickTransactionManaged(View view)
    {
        btnOnClick(view, TransactionMode.MANAGED);
    }

    public void btnOnClickTransactionAuto(View view)
    {
        btnOnClick(view, TransactionMode.AUTO);
    }

    private void btnOnClick(View view, TransactionMode transactionMode)
    {
        ParentEntity parent = new ParentEntity();
        parent.setTimestamp(new Date());


        List<ChildEntity> children = new LinkedList<>();
        for (int i = 1; i <= 5; i++)
        {
            ChildEntity child = new ChildEntity();
            child.setParent(parent);
            child.setName(String.format(Locale.getDefault(), "child %d", i));
        }

        ReactiveEntityStore<Persistable> data = ((App) getApplication()).getData(transactionMode);
        doInTransaction(data, parent, children);
    }

    private void doInTransaction(ReactiveEntityStore<Persistable> data, final ParentEntity parent,
                                 final List<ChildEntity> children)
    {

        final BlockingEntityStore store = data.toBlocking();
        Completable completable = Completable.fromCallable(new Callable<Void>()
        {
            @Override
            public Void call() throws Exception
            {
                store.runInTransaction(new Callable<Void>()
                {
                    @Override
                    public Void call() throws Exception
                    {
                        store.insert(parent);
                        store.insert(children);
                        return null;
                    }
                });
                return null;
            }
        });

        completable.subscribe();
    }


}
