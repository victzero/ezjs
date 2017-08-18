package me.ezjs.core.web.web.binder;

import me.ezjs.core.Constants;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ZeroDateEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) {
		if (text == null || text.length() == 0) {
			this.setValue(null);
			return;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATEFORMAT);// default:yyyy-MM-dd

		if (text.length() == 16) {// yyyy-MM-dd HH:mm:ss
			if (text.indexOf("-") != -1) {
				dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			} else if (text.indexOf("/") != -1) {
				dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			}
		} else if (text.length() == 10) {
			if (text.indexOf("/") != -1) {
				dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			}
		} else if (text.length() == 13) {
			Date date = new Date(Long.parseLong(text));
			this.setValue(date);
			return;
		}

		dateFormat.setLenient(false);

		try {
			this.setValue(dateFormat.parse(text));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
