package de.nebelhoernchen.requery558;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import de.nebelhoernchen.requery558.model.Child;
import de.nebelhoernchen.requery558.model.ChildEntity;
import de.nebelhoernchen.requery558.model.Parent;
import de.nebelhoernchen.requery558.model.ParentEntity;
import io.reactivex.Completable;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveResult;
import io.requery.sql.TransactionMode;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnOnClickStatements(View view)
    {
        runStatements(getData(null));
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
        doInTransaction(getData(transactionMode));
    }

    private ReactiveEntityStore<Persistable> getData(TransactionMode transactionMode)
    {
        App application = (App) getApplication();
        application.setTransactionMode(transactionMode);
        return application.getData();
    }

    @NonNull
    private ParentEntity getParentEntity()
    {
        ParentEntity parent = new ParentEntity();
        parent.setTimestamp(new Date());
        return parent;
    }

    @NonNull
    private List<ChildEntity> getChildEntities(ParentEntity parent)
    {
        List<ChildEntity> children = new LinkedList<>();
        for (int i = 1; i <= 5; i++)
        {
            ChildEntity child = new ChildEntity();
            child.setParent(parent);
            child.setName(String.format(Locale.getDefault(), "child %d", i));
            children.add(child);
        }
        return children;
    }

    private void runStatements(ReactiveEntityStore<Persistable> data)
    {
        ParentEntity parent = getParentEntity();
        List<ChildEntity> children = getChildEntities(parent);

        data.insert(parent).subscribe();
        data.insert(children).subscribe();

        showData(data);
    }

    private void doInTransaction(ReactiveEntityStore<Persistable> data)
    {
        final ParentEntity parent = getParentEntity();
        final List<ChildEntity> children = getChildEntities(parent);

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

        showData(data);
    }

    @SuppressLint("SetTextI18n")
    private void showData(ReactiveEntityStore<Persistable> data)
    {
        TextView textView = (TextView) findViewById(R.id.multilinetext);

        ReactiveResult<ParentEntity> parentEntities =
                data.select(ParentEntity.class).orderBy(ParentEntity.TIMESTAMP.desc()).get();

        List<ParentEntity> parents = parentEntities.toList();
        if (parents.isEmpty())
        {
            textView.setText("No data");
        }
        else
        {
            StringBuilder sb = new StringBuilder();
            String parentEntityDelimiter = "";
            for (Parent parent : parents)
            {
                sb.append(parentEntityDelimiter)
                  .append(String.format(Locale.getDefault(), "%s: Parent %d has children: ",
                          format(parent.getTimestamp()), parent.getID()));

                String childEntityDelimiter = "";
                List<Child> children = parent.getChildren().toList();
                for (Child child : children)
                {
                    sb.append(childEntityDelimiter).append(child.getID());
                    childEntityDelimiter = ",";
                }
                parentEntityDelimiter = "\n";
            }
            textView.setText(sb.toString());
        }
    }

    private String format(Date timestamp)
    {
        return DateUtils.formatDateTime(this, timestamp.getTime(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME);
    }
}
