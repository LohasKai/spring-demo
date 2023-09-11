package com.mhz.demo.service;


public interface TransferService {

    void transfer(String fromCardNo,String toCardNo,int money) throws Exception;
}
