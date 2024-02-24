package com.jiawa.train.member.service;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.jiawa.common.exception.BusinessException;
import com.jiawa.common.exception.BusinessExceptionEnum;
import com.jiawa.common.util.SnowUtil;
import com.jiawa.train.member.domain.Member;
import com.jiawa.train.member.domain.MemberExample;
import com.jiawa.train.member.mapper.MemberMapper;
import com.jiawa.train.member.req.MemberRegisterReq;
import com.jiawa.train.member.req.MemberSendCodeReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

   private static final Logger LOG= LoggerFactory.getLogger(MemberService.class);
    @Autowired
    private MemberMapper memberMapper;

    public int count(){
        return
                Math.toIntExact(memberMapper.countByExample(null));
    }



    public long register(MemberRegisterReq req){
        String mobile = req.getMobile();
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(memberExample);
        if(CollUtil.isNotEmpty(list)){

            //return list.get(0).getId();
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);

        }
        Member member = new Member();
        member.setId(SnowUtil.getSnowflakeNextId());
        member.setMobile(mobile);
        memberMapper.insert(member);
        return member.getId();
    }


    public void sendCode(MemberSendCodeReq req){
        String mobile = req.getMobile();
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(memberExample);

        //如果手机号不存在，则插入记录
        if(CollUtil.isEmpty(list)){
            LOG.info("手机号码不存在，插入一点记录");
            Member member = new Member();
            member.setId(SnowUtil.getSnowflakeNextId());
            member.setMobile(mobile);
            memberMapper.insert(member);
        }else {
            LOG.info("手机号码存在");

        }
        //生成验证码
        String s = RandomUtil.randomString(4);
        LOG.info("手机号码存在，验证码是：{}",s);
        LOG.info("保存短信记录表");

        //保存短信记录表：手机号，短信验证码，有效期，是否已经使用，业务类型，发送时间，使用时间

        //对接短信通道，发送短信
        LOG.info("对接短信通道，发送短信");

    }


}
