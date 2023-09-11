package com.mhz.demo.service.impl;


import com.mhz.demo.dao.AccountDao;
import com.mhz.demo.dao.impl.JdbcAccountDaoImpl;
import com.mhz.demo.factory.BeanFactory;
import com.mhz.demo.pojo.Account;
import com.mhz.demo.service.TransferService;
import com.mhz.demo.utils.TransactionManager;

public class TransferServiceImpl implements TransferService {

    private AccountDao accountDao = (AccountDao) BeanFactory.getBean("accountDao");

    @Override
    public void transfer(String fromCardNo, String toCardNo, int money) throws Exception {
//
//        try{
//            TransactionManager.getInstance().beginTransaction();
            Account from = accountDao.queryAccountByCardNo(fromCardNo);
            Account to = accountDao.queryAccountByCardNo(toCardNo);

            from.setMoney(from.getMoney()-money);
            to.setMoney(to.getMoney()+money);

            accountDao.updateAccountByCardNo(to);
            accountDao.updateAccountByCardNo(from);
//            TransactionManager.getInstance().commit();
//        }catch (Exception e){
//            e.printStackTrace();
//            TransactionManager.getInstance().rollback();
//            throw e;
//        }


    }
}
