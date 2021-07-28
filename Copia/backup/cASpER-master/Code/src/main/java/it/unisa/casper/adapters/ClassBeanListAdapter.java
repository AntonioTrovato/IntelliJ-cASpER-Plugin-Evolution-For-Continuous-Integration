package it.unisa.casper.adapters;

import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.ClassBeanList;

import java.util.ArrayList;
import java.util.List;

public class ClassBeanListAdapter implements ClassBeanList {
    public ClassBeanListAdapter(ArrayList<ClassBean> classBeanArrayList) {
        this.classBeanArrayList = classBeanArrayList;
    }

    @Override
    public List<ClassBean> getList() {
        return classBeanArrayList;
    }

    private ArrayList<ClassBean> classBeanArrayList = new ArrayList<ClassBean>();
}
