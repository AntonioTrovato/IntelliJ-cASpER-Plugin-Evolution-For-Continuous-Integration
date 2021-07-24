package it.unisa.casper.adapters;

import it.unisa.casper.storage.beans.MethodBean;
import it.unisa.casper.storage.beans.MethodBeanList;

import java.util.ArrayList;
import java.util.List;

public class MethodBeanListAdapter implements MethodBeanList {
    public MethodBeanListAdapter(ArrayList<MethodBean> methodBeanArrayList) {
        this.methodBeanArrayList = methodBeanArrayList;
    }

    @Override
    public List<MethodBean> getList() {
        return methodBeanArrayList;
    }

    private ArrayList<MethodBean> methodBeanArrayList = new ArrayList<MethodBean>();
}
