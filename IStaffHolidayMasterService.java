package com.jaw.staff.service;

import com.jaw.common.exceptions.DatabaseException;
import com.jaw.common.exceptions.DeleteFailedException;
import com.jaw.common.exceptions.DuplicateEntryException;
import com.jaw.common.exceptions.NoDataFoundException;
import com.jaw.common.exceptions.UpdateFailedException;
import com.jaw.common.exceptions.util.TableNotSpecifiedForAuditException;
import com.jaw.framework.appCache.ApplicationCache;
import com.jaw.framework.sessCache.SessionCache;
import com.jaw.framework.sessCache.UserSessionDetails;
import com.jaw.staff.controller.StaffHolidayMaster_MasterVO;

public interface IStaffHolidayMasterService {
	
	public void UpdateStaffHolidayMasterRec(StaffHolidayMaster_MasterVO master_MasterVO,
			UserSessionDetails userSessionDetails,SessionCache sessionCache,ApplicationCache applicationCache) 
			throws UpdateFailedException, NoDataFoundException, DuplicateEntryException,DatabaseException, TableNotSpecifiedForAuditException;
	public void DeleteStaffHolidayMasterRec(StaffHolidayMaster_MasterVO master_MasterVO,
			SessionCache sessionCache, ApplicationCache applicationCache)
			throws DeleteFailedException,NoDataFoundException, 
			DuplicateEntryException, DatabaseException, TableNotSpecifiedForAuditException;
	public void insertStaffHolidayMasterRec(StaffHolidayMaster_MasterVO master_MasterVO,
			SessionCache sessionCache) throws DuplicateEntryException, DatabaseException;
	public void SelectStaffHolidayMasterRec(StaffHolidayMaster_MasterVO master_MasterVO,
			SessionCache sessionCache)throws NoDataFoundException;
}
