package org.seckill.web;

import java.util.Date;
import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller // @service @Component
@RequestMapping("/seckill") // url: /ģ��/��Դ/{id}/ϸ�� ���磺/seckill/list
public class SeckillController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SeckillService seckillService;
	
	// list��������ɱ�б�ҳ
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public String list(Model model) {
		// ��ȡ�б�ҳ
		// model �������������Ⱦjspҳ�������
		// list.jsp + model = modelandviewer
		List<Seckill> list = seckillService.getSeckillList();
		model.addAttribute("list", list);
		return "list"; //WEB-INF/jsp/"list.jsp"
	}
	
	// detail ����ҳ��
	@RequestMapping(value="/{seckillId}/detail", method = RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
		if(seckillId == null) {
			return "redirect:/seckill/list";
		}
		Seckill seckill = seckillService.getById(seckillId);
		if(seckill == null) {
			return "forward:/seckill/list";
		}
		model.addAttribute("seckill", seckill);
		return "detail";
	}
	// �����ɱ��ַ�Ľӿ�
	// ajax�� json
	@ResponseBody // ���ע������������ͻᱻ��װ��json
	@RequestMapping(value="/{seckillId}/exposer",
					method=RequestMethod.POST,
					produces={"application/json;charset=UTF-8"}) //�������������������
	public SeckillResult<Exposer> exposer(Long seckillId) {
		SeckillResult<Exposer> result;
		try {
			Exposer exposer = seckillService.exportSeckillUrl(seckillId);
			result = new SeckillResult<Exposer>(true, exposer);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = new SeckillResult<Exposer>(false, e.getMessage());
		}
		return result;
	}
	
	// ִ����ɱ
	@ResponseBody // ���ע������������ͻᱻ��װ��json
	@RequestMapping(value="/{seckillId}/{md5}/execution",
			method=RequestMethod.POST,
			produces={"application/json;charset=UTF-8"}) // ��֪�������������Ϣ
	public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,@PathVariable("md5") String md5, @CookieValue(value="killPhone", required = false) Long userPhone) {
		// �û����ֻ������Ǵ�cookie�еõ�
		// springmvc ��֤��ʽͬ��Ҳ���ԣ��������ﲻ��Ҫ
		if(userPhone == null) {
			return new SeckillResult<SeckillExecution>(false, "δע��");
		}
		SeckillResult<SeckillExecution> result;
		try {
			SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, userPhone, md5);
			return new SeckillResult<SeckillExecution>(true, seckillExecution);
		} catch(RepeatKillException e) {
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
			return new SeckillResult<SeckillExecution>(false, execution);
		} catch (SeckillCloseException e) {
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
			return new SeckillResult<SeckillExecution>(false, execution);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
			return new SeckillResult<SeckillExecution>(false, execution);
		}
	}
	// ��ȡϵͳʱ��
	@RequestMapping(value="/time/now", method=RequestMethod.GET)
	public SeckillResult<Long> time() {
		Date now = new Date();
		return new SeckillResult(true, now.getTime());
	}
}
