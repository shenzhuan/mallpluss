package com.zscat.mallplus.oms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.enums.AllEnum;
import com.zscat.mallplus.enums.OrderStatus;
import com.zscat.mallplus.oms.entity.OmsOrder;
import com.zscat.mallplus.oms.entity.OmsOrderOperateHistory;
import com.zscat.mallplus.oms.entity.OmsOrderReturnApply;
import com.zscat.mallplus.oms.mapper.OmsOrderMapper;
import com.zscat.mallplus.oms.mapper.OmsOrderReturnApplyMapper;
import com.zscat.mallplus.oms.service.IOmsOrderOperateHistoryService;
import com.zscat.mallplus.oms.service.IOmsOrderReturnApplyService;
import com.zscat.mallplus.oms.vo.OmsUpdateStatusParam;
import com.zscat.mallplus.utils.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 订单退货申请 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
@Service
public class OmsOrderReturnApplyServiceImpl extends ServiceImpl<OmsOrderReturnApplyMapper, OmsOrderReturnApply> implements IOmsOrderReturnApplyService {
    @Autowired
    private OmsOrderReturnApplyMapper returnApplyMapper;
    @Autowired
    private IOmsOrderOperateHistoryService orderOperateHistoryService;
    @Autowired
    private OmsOrderMapper orderMapper;

    @Transactional
    @Override
    public int updateStatus(Long id, OmsUpdateStatusParam statusParam) {
        Integer status = statusParam.getStatus();
        OmsOrderReturnApply returnApply = returnApplyMapper.selectById(id);
       /* OmsOrder order = orderMapper.selectById(returnApply.getOrderId());
        if (order==null){
            return 0;
        }*/
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        String  typeStr ="换";
        if (returnApply.getType()==0){
            typeStr ="换货";
        }else  if (returnApply.getType()==1){
            typeStr ="退钱";
        }else  if (returnApply.getType()==2){
            typeStr ="退货";
        }else  if (returnApply.getType()==3){
            typeStr ="退钱退货";
        }
        if (status.equals(1)) {
           /* if (returnApply.getType()==1 ||returnApply.getType()==3){
                order.setStatus(OrderStatus.REFUND.getValue());
            }*/
            //确认退货
            history.setNote("确认"+typeStr);
            returnApply.setId(id);
            returnApply.setStatus(AllEnum.OmsOrderReturnApplyStatus.REFUNDING.code());
            returnApply.setRefundStatus(1);
            if (returnApply.getType()==1 ||returnApply.getType()==3){ // 0换货 1退钱 2退货3 退钱退货
                returnApply.setReturnAmount(statusParam.getReturnAmount());
            }
            if (returnApply.getType()!=1){ // 0换货 1退钱 2退货3 退钱退货
                if (ValidatorUtils.empty(statusParam.getCompanyAddressId()) ||statusParam.getCompanyAddressId()==0){
                }else {
                    returnApply.setCompanyAddressId(statusParam.getCompanyAddressId());
                }
            }
            returnApply.setHandleTime(new Date());
            returnApply.setHandleMan(statusParam.getHandleMan());
            returnApply.setHandleNote(statusParam.getHandleNote());
        } else if (status.equals(2)) {
            //完成退货
            history.setNote("完成"+typeStr);
            returnApply.setId(id);
            returnApply.setStatus(AllEnum.OmsOrderReturnApplyStatus.REFUNDED.code());
            returnApply.setReceiveTime(new Date());
            returnApply.setReceiveMan(statusParam.getReceiveMan());
            returnApply.setReceiveNote(statusParam.getReceiveNote());
        } else if (status.equals(3)) {
            //拒绝退货
            history.setNote("拒绝"+typeStr);
            returnApply.setId(id);
            returnApply.setStatus(AllEnum.OmsOrderReturnApplyStatus.REJECT.code());
            returnApply.setHandleTime(new Date());
            returnApply.setHandleMan(statusParam.getHandleMan());
            returnApply.setHandleNote(statusParam.getHandleNote());
        } else {
            return 0;
        }
        history.setOrderId(statusParam.getOrderId());
        history.setCreateTime(new Date());
        history.setOperateMan("admin");

        orderOperateHistoryService.save(history);
        return returnApplyMapper.updateById(returnApply);
    }
}
