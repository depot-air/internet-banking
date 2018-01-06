package com.dwidasa.admin.pages.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry5.upload.services.UploadedFile;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.common.BaseDataSource;
import com.dwidasa.admin.pages.user.ChangeProfile;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.model.AccountType;
import com.dwidasa.engine.model.FileBatch;
import com.dwidasa.engine.model.FileBatch.BatchStatus;
import com.dwidasa.engine.model.User;
import com.dwidasa.engine.service.FileBatchService;
import com.dwidasa.engine.service.UserService;
import com.dwidasa.engine.service.VersionService;

@Restricted(groups = { Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER })
public class FileBatchList {

	@Inject
	private HttpServletRequest request;

	@Property
	private UploadedFile file;

	@Property
	private String fileName;

	@Property
	private Date notifDate;

	@Property
	private FileBatch fileBatch;

	@Property
	private String title;

	@Property
	private String Content;

    @Inject
    private BeanModelSource beanModelSource;

    @Property
	private GridDataSource dataSource;

	@Property
	private int pageSize;

	@Inject
	private VersionService versionService;

	@Inject
	private FileBatchService fileBatchService;

	@Inject
	private Messages messages;

	@Property
	@Persist
	private String Subject;

	@Persist
	@Property(write = false)
	private Long id;

	@Inject
	private SessionManager sessionManager;

	void onActivate() {
		id = sessionManager.getLoggedUser().getId();
	}

	// void onPrepare() {
	// if (id != null) {
	// fileBatch = fileBatchService.get(id);
	// }else{
	// fileBatch = new FileBatch();
	// }
	// }

	public BeanModel<FileBatch> getModel() {
        final BeanModel<FileBatch> model = beanModelSource.createDisplayModel(FileBatch.class, messages);

        model.include("fileName", "uploadDate", "notifDate", "statusNama");
        // Set labels
        model.get("fileName").label("Nama File");
        model.get("uploadDate").label("Tgl. Unggah");
        model.get("notifDate").label("Tgl. Posting");
        model.get("statusNama").label("Status");

        return model;
    }

	void setupRender() {
		pageSize = Constants.PAGE_SIZE;

		List<String> restrictions = new ArrayList<String>();
		List values = new ArrayList();

		if (Subject != null && !Subject.equals("")) {
			restrictions.add("upper(title) like '%' || ? || '%'");
			values.add(Subject.toUpperCase());
		}

		if (id == null) {
			fileBatch = new FileBatch();
		}

		dataSource = new BaseDataSource(FileBatch.class, Constants.PAGE_SIZE,
				restrictions, values);
	}

	// public String getRoleName() {
	// if (user == null || user.getRoleId() == null) return "-";
	// if (Constants.Role.SUPERUSER.equals(user.getRoleId())) {
	// return Constants.RoleName.SUPERUSER;
	// } else if (Constants.Role.ADMIN.equals(user.getRoleId())) {
	// return Constants.RoleName.ADMIN;
	// } else if (Constants.Role.TREASURY.equals(user.getRoleId())) {
	// return Constants.RoleName.TREASURY;
	// } else if (Constants.Role.DAY_ADMIN.equals(user.getRoleId())) {
	// return Constants.RoleName.DAY_ADMIN;
	// } else if (Constants.Role.NIGHT_ADMIN.equals(user.getRoleId())) {
	// return Constants.RoleName.NIGHT_ADMIN;
	// }
	// return "-";
	// }

	Object onSuccess() {
		File uploadPath = buildUploadPath();
		File copied = new File(uploadPath, file.getFileName());
		file.write(copied);

		FileBatch newFile = new FileBatch();
		newFile.setFileName(file.getFileName());
		newFile.setStatus(0);
		newFile.setUploadDate(new Date());
		newFile.setNotifDate(notifDate);
		newFile.setCreated(new Date());
		newFile.setCreatedby(sessionManager.getLoggedUser().getId());
		newFile.setUpdated(new Date());
		newFile.setUpdatedby(sessionManager.getLoggedUser().getId());
		fileBatchService.save(newFile);

		return null;
	}

	@Persist(PersistenceConstants.FLASH)
	private String messageInfo;

	public String getMessageInfo() {
		return messageInfo;
	}

	public void setMessageInfo(String messageInfo) {
		this.messageInfo = messageInfo;
	}

	@Environmental
	private JavaScriptSupport renderSupport;

	@DiscardAfter
	void onActionFromDelete(Long id, String fileName) {
		try {
			File uploadPath = buildUploadPath();
			File copied = new File(uploadPath, fileName);
			copied.delete();
		} catch (Exception ex) {
			System.out.print(ex.getMessage());
		}

		FileBatch tt = new FileBatch();
		tt.setId(id);
		Long userId = sessionManager.getLoggedUser().getId();
		// versionService.versionedRemove(tt, fileBatchService, userId);
		fileBatchService.remove(id, userId);

	}

	private File buildUploadPath() {

		// write the file to the filesystem the directory to upload to
		String uploadDir = String.format("%s%s", request.getSession()
				.getServletContext().getRealPath("/uploads"),
				System.getProperty("file.separator"));
		System.out.println("Upload dir..." + uploadDir);

		// Create the directory if it doesn't exist
		File dirPath = new File(uploadDir);

		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		return dirPath;
	}

	@DiscardAfter
	void onActionFromApprove(Long id) {
		try {
			FileBatch fb = fileBatchService.get(id);
			fb.setStatus(BatchStatus.approve.getId());
			fileBatchService.save(fb);
		} catch (Exception ex) {
			System.out.print(ex.getMessage());
		}
	}

	@DiscardAfter
	void onActionFromReject(Long id) {
		try {
			FileBatch fb = fileBatchService.get(id);
			fb.setStatus(BatchStatus.reject.getId());
			fileBatchService.save(fb);
		} catch (Exception ex) {
			System.out.print(ex.getMessage());
		}
	}

	@DiscardAfter
	void onActionFromPending(Long id) {
		try {
			FileBatch fb = fileBatchService.get(id);
			fb.setStatus(BatchStatus.pending.getId());
			fileBatchService.save(fb);
		} catch (Exception ex) {
			System.out.print(ex.getMessage());
		}
	}

	@DiscardAfter
	void onActionFromExecute(Long id) {
		try {
			File uploadPath = buildUploadPath();			
			fileBatchService.processFileBatchInsertToInboxAndNotify(id,
					uploadPath.getAbsolutePath(),
					sessionManager.getLoggedUser().getId());

		} catch (Exception ex) {
			System.out.print(ex.getMessage());
		}
	}

}
