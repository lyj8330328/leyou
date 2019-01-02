package com.leyou.order.service.impl;

import com.leyou.auth.entity.UserInfo;
import com.leyou.order.interceptor.LoginInterceptor;
import com.leyou.order.mapper.AddressMapper;
import com.leyou.order.pojo.Address;
import com.leyou.order.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author: 98050
 * @Time: 2018-10-31 09:44
 * @Feature:
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public void deleteAddress(Long addressId) {
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        Example example = new Example(Address.class);
        example.createCriteria().andEqualTo("userId",userInfo.getId()).andEqualTo("id",addressId);
        this.addressMapper.deleteByExample(example);
    }

    @Override
    public void updateAddressByUserId(Address address) {
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        address.setUserId(userInfo.getId());
        setDefaultAddress(address);
        this.addressMapper.updateByPrimaryKeySelective(address);

    }

    @Override
    public List<Address> queryAddressByUserId() {
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        Example example = new Example(Address.class);
        example.createCriteria().andEqualTo("userId",userInfo.getId());
        return this.addressMapper.selectByExample(example);
    }

    @Override
    public void addAddressByUserId(Address address) {
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        address.setUserId(userInfo.getId());
        setDefaultAddress(address);
        this.addressMapper.insert(address);
    }

    @Override
    public Address queryAddressById(Long addressId) {
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        Example example = new Example(Address.class);
        example.createCriteria().andEqualTo("id",addressId).andEqualTo("userId",userInfo.getId());
        return this.addressMapper.selectByExample(example).get(0);
    }

    public void setDefaultAddress(Address address){
        if (address.getDefaultAddress()){
            //如果将本地址设置为默认地址，那么该用户下的其他地址都应该是非默认地址
            List<Address> addressList = this.queryAddressByUserId();
            addressList.forEach(addressTemp -> {
                if (addressTemp.getDefaultAddress()){
                    addressTemp.setDefaultAddress(false);
                    this.addressMapper.updateByPrimaryKeySelective(addressTemp);
                }
            } );
        }
    }
}
