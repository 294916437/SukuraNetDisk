package easypan.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import easypan.entity.vo.PaginationResultVO;
import easypan.exception.BusinessException;
import easypan.entity.po.EmailCode;
import easypan.entity.po.UserInfo;
import easypan.component.RedisComponent;
import easypan.entity.Constants.Constants;
import easypan.entity.config.AppConfig;
import easypan.entity.dto.SysSettingDto;
import easypan.entity.enums.PageSize;
import easypan.entity.query.EmailCodeQuery;
import easypan.entity.query.SimplePage;
import easypan.entity.query.UserInfoQuery;
import easypan.service.EmailCodeService;
import easypan.utils.StringTools;
import easypan.mappers.EmailCodeMapper;
import easypan.mappers.UserInfoMapper;

/**
 * @Description:Service
 * @Author: 春的樱树
 * @Date: 2024-02-23
 */
@Service("emailCodeService")
public class EmailCodeServiceImpl implements EmailCodeService {
    private static Logger logger = LoggerFactory.getLogger(EmailCodeServiceImpl.class);
    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private AppConfig appConfig;
    @Resource
    private EmailCodeMapper<EmailCode, EmailCodeQuery> emailCodeMapper;
    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;
    @Resource
    private RedisComponent redisComponent;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<EmailCode> findListByParam(EmailCodeQuery query) {
        return this.emailCodeMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    @Override
    public Integer findCountByParam(EmailCodeQuery query) {
        return this.emailCodeMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
    @Override
    public PaginationResultVO<EmailCode> findListByPage(EmailCodeQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<EmailCode> list = this.findListByParam(query);
        PaginationResultVO<EmailCode> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(),
                page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(EmailCode bean) {
        return this.emailCodeMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<EmailCode> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.emailCodeMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或修改
     */
    @Override
    public Integer addOrUpdateBatch(List<EmailCode> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.emailCodeMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 根据EmailAndCode查询
     */
    @Override
    public EmailCode getByEmailAndCode(String email, String code) {
        return this.emailCodeMapper.selectByEmailAndCode(email, code);
    }

    /**
     * 根据EmailAndCode更新
     */
    @Override
    public Integer updateByEmailAndCode(EmailCode bean, String email, String code) {
        return this.emailCodeMapper.updateByEmailAndCode(bean, email, code);
    }

    /**
     * 根据EmailAndCode删除
     */
    @Override
    public Integer deleteByEmailAndCode(String email, String code) {
        return this.emailCodeMapper.deleteByEmailAndCode(email, code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendEmailCode(String email, Integer type) throws BusinessException {
        if (type == Constants.ZERO) {
            UserInfo userInfo = userInfoMapper.selectByEmail(email);
            if (userInfo != null) {
                throw new BusinessException("邮箱已经存在");
            }
        }
        String code = StringTools.getRandomNumber(Constants.LENGTH_5);
        //发送邮箱验证码
        sendEmailCode(email, code);
        // 将之前的验证码置无效
        emailCodeMapper.disableEmailCode(email);
        //保存数据库信息
        EmailCode emailcode = new EmailCode();
        emailcode.setCode(code);
        emailcode.setEmail(email);
        emailcode.setStatus(Constants.ZERO);
        emailcode.setCreateTime(new Date());
        emailCodeMapper.insert(emailcode);

    }

    private void sendEmailCode(String toEmail, String code) throws BusinessException {
        try {
            SysSettingDto sysSettingDto = new SysSettingDto();
            sysSettingDto.setRegisterEmailContent(String.format(sysSettingDto.getRegisterEmailContent(), code));
            sysSettingDto.setEmail(toEmail);
            redisComponent.saveSysSettingDto(sysSettingDto);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(appConfig.getSendUserName(),appConfig.getNickName());
            helper.setTo(toEmail);
            sysSettingDto = redisComponent.getSysSettingDto();
            helper.setSubject(sysSettingDto.getRegisterEmailTitle());
            String htmlContent ="尊敬的用户，\n\n" +
                                "您的验证码为：" + code + "\n\n"  +
                                "请在 5 分钟内使用此验证码完成操作。\n\n"  +
                                "若非您本人操作，请忽略此邮件。\n\n"  +
                                "祝您使用愉快！\n\n";
            helper.setText(htmlContent);
            helper.setSentDate(new Date());
            javaMailSender.send(message);
        } catch (Exception e) {
            logger.error("邮件发送失败", e);
            throw new BusinessException("邮件发送失败");
        }
    }

    @Override
    public void checkCode(String email, String code) throws BusinessException {
        EmailCode emailCode = this.emailCodeMapper.selectByEmailAndCode(email, code);
        if (emailCode == null) {
            throw new BusinessException("邮箱验证码不正确");
        }
        if (emailCode.getStatus() == 1 || System.currentTimeMillis() - emailCode.getCreateTime().getTime() > Constants.LENGTH_5 * 1000 * 60) {
            throw new BusinessException("邮箱验证码已过期");
        }
    }

}