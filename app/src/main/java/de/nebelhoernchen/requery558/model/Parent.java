package de.nebelhoernchen.requery558.model;

import java.util.Date;

import io.requery.CascadeAction;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.OneToMany;
import io.requery.Table;
import io.requery.query.Result;

@Table(name = "Parent")
@Entity(name = "ParentEntity")
public interface Parent
{
    @Key
    @Generated
    int getID();

    Date getTimestamp();

    @OneToMany(mappedBy = "parent", cascade = {CascadeAction.DELETE, CascadeAction.SAVE})
    Result<Child> getChildren();

}
