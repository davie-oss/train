package com.jiawa.train.member.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.jiawa.train.common.exception.BusinessException;
import com.jiawa.train.common.exception.BusinessExceptionEnum;
import com.jiawa.train.common.util.JwtUtil;
import com.jiawa.train.common.util.SnowUtil;
import com.jiawa.train.member.domain.Member;
import com.jiawa.train.member.domain.MemberExample;
import com.jiawa.train.member.mapper.MemberMapper;
import com.jiawa.train.member.req.MemberLoginReq;
import com.jiawa.train.member.req.MemberRegisterReq;
import com.jiawa.train.member.req.MemberSendCodeReq;
import com.jiawa.train.member.resp.MemberLoginResp;
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
        Member memberDB = selectByMobile(mobile);

        //如果手机号不存在，则插入记录
        if(ObjectUtil.isNull(memberDB)){
            LOG.info("手机号码不存在，插入一点记录");
            Member member = new Member();
            member.setId(SnowUtil.getSnowflakeNextId());
            member.setMobile(mobile);
            memberMapper.insert(member);
        }else {
            LOG.info("手机号码存在");

        }
        //生成验证码
        /*String s = RandomUtil.randomString(4);*/
        String s="8888";
        LOG.info("保存短信记录表");

        //保存短信记录表：手机号，短信验证码，有效期，是否已经使用，业务类型，发送时间，使用时间

        //对接短信通道，发送短信
        LOG.info("对接短信通道，发送短信");

    }

    public MemberLoginResp login(MemberLoginReq req){
        String mobile = req.getMobile();
        String code = req.getCode();
        Member memberDB = selectByMobile(mobile);

        //如果手机号不存在，则插入记录
        if(ObjectUtil.isNull(memberDB)){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_NOT_EXIST);
        }

        //验证短信验证码
        if(!"8888".equals(code)){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_CODE_ERROR);
        }

        MemberLoginResp memberLoginResp = BeanUtil.copyProperties(memberDB, MemberLoginResp.class);
        String token = JwtUtil.createToken(memberLoginResp.getId(), memberLoginResp.getMobile());
        memberLoginResp.setToken(token);
        return memberLoginResp;
    }
  private  Member selectByMobile(String mobile){
      MemberExample memberExample = new MemberExample();
      memberExample.createCriteria().andMobileEqualTo(mobile);
      List<Member> list = memberMapper.selectByExample(memberExample);
      if (CollUtil.isEmpty(list)) {
          return null;
      } else {
          return list.get(0);
      }
  }

}
