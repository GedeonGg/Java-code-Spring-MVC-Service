package com.jaw.staff.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaw.common.business.CommonBusiness;
import com.jaw.common.constants.AuditConstant;
import com.jaw.common.constants.TableNameConstant;
import com.jaw.common.exceptions.DatabaseException;
import com.jaw.common.exceptions.DeleteFailedException;
import com.jaw.common.exceptions.DuplicateEntryException;
import com.jaw.common.exceptions.NoDataFoundException;
import com.jaw.common.exceptions.UpdateFailedException;
import com.jaw.common.exceptions.util.TableNotSpecifiedForAuditException;
import com.jaw.common.util.DateUtil;
import com.jaw.framework.appCache.ApplicationCache;
import com.jaw.framework.audit.service.DoAudit;
import com.jaw.framework.sessCache.SessionCache;
import com.jaw.framework.sessCache.UserSessionDetails;
import com.jaw.staff.controller.StaffHolidayMasterListVO;
import com.jaw.staff.controller.StaffHolidayMaster_MasterVO;
import com.jaw.staff.dao.IStaffHolidayMasterDAO;
import com.jaw.staff.dao.IStaffHolidayMasterListDao;
import com.jaw.staff.dao.StaffHolidayMaster;
import com.jaw.staff.dao.StaffHolidayMasterKey;
import com.jaw.staff.dao.StaffHolidayMasterList;
import com.jaw.staff.dao.StaffHolidayMasterListKey;

@Service
public class StaffHolidayMasterService implements IStaffHolidayMasterService {
	@Autowired
	CommonBusiness commonBusiness;
	@Autowired
	IStaffHolidayMasterDAO holidayMasterDAO;
	@Autowired
	DateUtil dateUtil;
	@Autowired
	IStaffHolidayMasterListDao listDao;
	@Autowired
	DoAudit doAudit;
	Logger logger=Logger.getLogger(StaffHolidayMasterService.class);

	@Transactional(rollbackFor=Exception.class)
	@Override
	public void DeleteStaffHolidayMasterRec(
			StaffHolidayMaster_MasterVO master_MasterVO,
			SessionCache sessionCache, ApplicationCache applicationCache)
					throws DeleteFailedException, NoDataFoundException,
					DuplicateEntryException, DatabaseException, TableNotSpecifiedForAuditException {
		// TODO Auto-generated method stub
		StaffHolidayMasterKey key=new StaffHolidayMasterKey();
		key.setInstId(sessionCache.getUserSessionDetails().getInstId());
		key.setBranchId(sessionCache.getUserSessionDetails().getBranchId());
		key.setStfGrp(sessionCache.getStaffSession().getStfGrpId());
		key.setHolDate(dateUtil.convertUIToDBDateFormat(master_MasterVO.getHolidayMasterVO().getHolDate()));
		key.setStfLvCat(master_MasterVO.getMaster_SearchVO().getStfLvCat());
		StaffHolidayMaster stfHolidayDataNew=holidayMasterDAO.selectStaffHolidayMasterRec(key);
		key.setDbTs(stfHolidayDataNew.getDbTs());
		stfHolidayDataNew.setDbTs(stfHolidayDataNew.getDbTs());
		stfHolidayDataNew.setrModId(stfHolidayDataNew.getrModId());
		holidayMasterDAO.deleteStaffHolidayMasterRec(stfHolidayDataNew, key);

		doAudit.doFunctionalAudit(sessionCache.getUserSessionDetails(), AuditConstant.STAFF_GENERAL_HOLIDAY_DELETE, "");
	}
	@Override
	public void insertStaffHolidayMasterRec(
			StaffHolidayMaster_MasterVO master_MasterVO,
			SessionCache sessionCache) throws DuplicateEntryException,
			DatabaseException {
		// TODO Auto-generated method stub
		StaffHolidayMaster staffHolidayMaster=new StaffHolidayMaster();
		commonBusiness.changeObject(staffHolidayMaster, master_MasterVO.getHolidayMasterVO());
		staffHolidayMaster.setDbTs(1);
		staffHolidayMaster.setInstId(sessionCache.getUserSessionDetails().getInstId());
		staffHolidayMaster.setStfLvCat(master_MasterVO.getMaster_SearchVO().getStfLvCat());
		staffHolidayMaster.setBranchId(sessionCache.getUserSessionDetails().getBranchId());
		staffHolidayMaster.setStfGrp(sessionCache.getStaffSession().getStfGrpId());
		staffHolidayMaster.setrCreId(sessionCache.getUserSessionDetails().getUserId());
		staffHolidayMaster.setrModId(sessionCache.getUserSessionDetails().getUserId());
		staffHolidayMaster.setDelFlg("N");
		staffHolidayMaster.setHolDate(master_MasterVO.getHolidayMasterVO().getHolDate());
		staffHolidayMaster.setHolDesc(master_MasterVO.getHolidayMasterVO().getHolDesc());
		holidayMasterDAO.insertStaffHolidayMasterRec(staffHolidayMaster);

		doAudit.doFunctionalAudit(sessionCache.getUserSessionDetails(), AuditConstant.STAFF_GENERAL_HOLIDAY_INSERT, "");
	}
	@Override
	public void SelectStaffHolidayMasterRec(
			StaffHolidayMaster_MasterVO master_MasterVO,
			SessionCache sessionCache) throws NoDataFoundException {
		// TODO Auto-generated method stub
		StaffHolidayMasterListKey listKey= new StaffHolidayMasterListKey();
		listKey.setBranchId(sessionCache.getUserSessionDetails().getBranchId());
		if(sessionCache.getStaffSession()!=null)
		{
		listKey.setStfGrp(sessionCache.getStaffSession().getStfGrpId());
		}
		listKey.setInstId(sessionCache.getUserSessionDetails().getInstId());
		listKey.setHolDate(master_MasterVO.getHolidayMasterVO().getHolDate());
		listKey.setStfLvCat(master_MasterVO.getMaster_SearchVO().getStfLvCat());
		listKey.setFromDate(dateUtil.convertUIToDBDateFormat(master_MasterVO.getMaster_SearchVO().getFromDate()));
		listKey.setToDate(dateUtil.convertUIToDBDateFormat(master_MasterVO.getMaster_SearchVO().getToDate()));
		List<StaffHolidayMasterList> staffHoliMasterList=listDao.getListForHolidayListRec(listKey);
		List<StaffHolidayMasterListVO> staffLateRuleVOs=new ArrayList<StaffHolidayMasterListVO>();
		int row=0;
		for(int i=0;i<staffHoliMasterList.size();i++)
		{
			StaffHolidayMasterListVO holidayMasterListVO=new StaffHolidayMasterListVO();
			commonBusiness.changeObject(holidayMasterListVO, staffHoliMasterList.get(i));
			holidayMasterListVO.setRowId(row);
			row++;
			staffLateRuleVOs.add(holidayMasterListVO);
		}
		master_MasterVO.setHolidayMasterListVOs(staffLateRuleVOs);
	}
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void UpdateStaffHolidayMasterRec(
			StaffHolidayMaster_MasterVO master_MasterVO,
			UserSessionDetails userSessionDetails, SessionCache sessionCache, ApplicationCache applicationCache)
					throws UpdateFailedException, NoDataFoundException,
					DuplicateEntryException, DatabaseException, TableNotSpecifiedForAuditException {
		// TODO Auto-generated method stub
		StaffHolidayMasterKey key=new StaffHolidayMasterKey();
		key.setInstId(sessionCache.getUserSessionDetails().getInstId());
		key.setBranchId(sessionCache.getUserSessionDetails().getBranchId());
		key.setStfGrp(sessionCache.getStaffSession().getStfGrpId());
		key.setHolDate(dateUtil.convertUIToDBDateFormat(master_MasterVO.getHolidayMasterVO().getHolDate()));
		key.setStfLvCat(master_MasterVO.getMaster_SearchVO().getStfLvCat());
		StaffHolidayMaster stfHolidayDataNew=holidayMasterDAO.selectStaffHolidayMasterRec(key);
		String oldString=stfHolidayDataNew.StaffHolidayMasterForDbAudit();
		key.setDbTs(stfHolidayDataNew.getDbTs());
		stfHolidayDataNew.setDbTs(stfHolidayDataNew.getDbTs());
		stfHolidayDataNew.setHolDesc(master_MasterVO.getHolidayMasterVO().getHolDesc());
		stfHolidayDataNew.setrModId(stfHolidayDataNew.getrModId());
		holidayMasterDAO.updateStaffHolidayMasterRec(stfHolidayDataNew, key);

		doAudit.doFunctionalAudit(sessionCache.getUserSessionDetails(), AuditConstant.STAFF_GENERAL_HOLIDAY_UPDATE, "");

		
		key.setDbTs(0);
		StaffHolidayMaster newData=holidayMasterDAO.selectStaffHolidayMasterRec(key);
		String newRecString=newData.StaffHolidayMasterForDbAudit();

		doAudit.doDatabaseAudit(applicationCache ,sessionCache.getUserSessionDetails(), TableNameConstant.STAFF_GENERAL_HOLIDAY,
				key.StaffHolidayMasterKeyForDbAudit(), oldString, AuditConstant.TYPE_OF_OPER_UPDATE, newRecString, "");
	}
}
