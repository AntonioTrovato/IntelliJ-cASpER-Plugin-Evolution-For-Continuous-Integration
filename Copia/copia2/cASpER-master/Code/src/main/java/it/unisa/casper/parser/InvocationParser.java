package it.unisa.casper.parser;

import it.unisa.casper.beans.MethodBean;

public class InvocationParser {

    public static MethodBean parse(String pInvocationName) {
        MethodBean methodBean = new MethodBean();
        methodBean.setName(pInvocationName);
        return methodBean;
    }

}
