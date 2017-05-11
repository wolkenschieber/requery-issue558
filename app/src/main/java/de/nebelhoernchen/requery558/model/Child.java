package de.nebelhoernchen.requery558.model;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.Table;

@Table(name = "Child")
@Entity(name = "ChildEntity")
public interface Child
{
    @Key
    @Generated
    int getID();

    @ForeignKey
    @ManyToOne
    Parent getParent();

    String getName();
}
