package com.mei.zhuang.service.goods.impl;

import com.arvato.service.goods.api.orm.dao.EsShopCardMessageCutMapper;
import com.arvato.service.goods.api.orm.dao.EsShopCardMessageMapper;
import com.arvato.service.goods.api.service.EsShopCardMessageServer;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.goods.EsShopCardMessage;
import com.mei.zhuang.entity.goods.EsShopCardMessageCut;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EsShopCardMessageServerImpl extends ServiceImpl<EsShopCardMessageMapper, EsShopCardMessage> implements EsShopCardMessageServer {

    @Resource
    private EsShopCardMessageCutMapper esShopCardMessageCutMapper;

    @Override
    public Object save(EsShopCardMessage entity) {
       if(this.insert(entity)){
           if(entity.getListCut()!=null && entity.getListCut().size()>0){
               EsShopCardMessageCut messageCut = new EsShopCardMessageCut();
               messageCut.setCardMessageId(entity.getId());
               for (EsShopCardMessageCut cut:entity.getListCut()) {
                   esShopCardMessageCutMapper.insert(cut);
               }
           }
            return true;
       }
       return false;
    }

    @Override
    public Object updates(EsShopCardMessage entity) {
        try{
            if(this.updateById(entity)){
                System.out.println("数据打印："+entity.getId());
                EsShopCardMessageCut messageCut = new EsShopCardMessageCut();
                messageCut.setCardMessageId(entity.getId());
                esShopCardMessageCutMapper.delete(new QueryWrapper<>(messageCut));
                if(entity.getListCut() != null){
                    for (EsShopCardMessageCut cut:entity.getListCut()) {
                        cut.setCardMessageId(entity.getId());
                        esShopCardMessageCutMapper.insert(cut);
                    }
                }

                return true;
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public EsShopCardMessage selPageList() {

        EsShopCardMessage message=this.selectOne(new QueryWrapper<>());
        if(message!=null){
            EsShopCardMessageCut messageCut = new EsShopCardMessageCut();
            messageCut.setCardMessageId(message.getId());
            message.setListCut(esShopCardMessageCutMapper.selectList(new QueryWrapper<>(messageCut)));
        }

        return message;
    }
}
