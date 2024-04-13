package easypan.entity.enums;

import org.apache.commons.lang3.ArrayUtils;

public enum FileTypeEnums {
	VIDEO(FileCategoryEnums.VIDEO, 1, new String[] { ".mp4", ".avi", ".rmvb", ".mkv", ".mov" }, "视频"),
	MUSIC(FileCategoryEnums.MUSIC, 2,
			new String[] { ".mp3",".ogg", ".wav", ".wma", ".mp2"}, "音频"),
	IMAGE(FileCategoryEnums.IMAGE, 3,
			new String[] { ".jpeg", ".jpg", ".bmp", ".dds", ".psd", ".pdt", ".webp", ".xmp", ".svp", ".tiff" }, "图片"),
	PDF(FileCategoryEnums.DOC, 4, new String[] { ".pdf" }, "pdf"),
	WORD(FileCategoryEnums.DOC, 5, new String[] { ".docx" }, "word"),
	EXCEL(FileCategoryEnums.DOC, 6, new String[] { ".xlsx" }, "excel"),
	TXT(FileCategoryEnums.DOC, 7, new String[] { ".txt" }, "txt文本"),
	code(FileCategoryEnums.OTHERS, 8,
			new String[] { ".h", ".c", ".hpp", ".hxx", ".cpp", ".cc", ".c++", ".m", ".o", ".s", ".dll", ".cs", ".java",
					".class", ".js", ".ts", ".css", ".scss", ".vue", ".jsx", ".sql", ".md", ".json", ".html", ".xml" },
			"CODE"),
	ZIP(FileCategoryEnums.DOC, 9, new String[] { ".rar", ".zip", ".7z", ".jar", ".iso", ".mpq" }, "压缩包"),
	OTHERS(FileCategoryEnums.DOC, 10, new String[] {}, "其他");

	private FileCategoryEnums category;
	private Integer type;
	private String[] suffix;
	private String desc;

	FileTypeEnums(FileCategoryEnums category, Integer type, String[] suffix, String desc) {
		this.category = category;
		this.type = type;
		this.suffix = suffix;
		this.desc = desc;
	}

	public FileCategoryEnums getCategory() {
		return category;
	}

	public Integer getType() {
		return type;
	}

	public String[] getSuffix() {
		return suffix;
	}

	public String getDesc() {
		return desc;
	}

	public static FileTypeEnums getFileTypeBySuffix(String suffix) {
		for (FileTypeEnums item : FileTypeEnums.values()) {
			if (ArrayUtils.contains(item.getSuffix(), suffix)) {
				return item;
			}
		}
		return FileTypeEnums.OTHERS;
	}

	public static FileTypeEnums getByType(Integer type) {
		for (FileTypeEnums item : FileTypeEnums.values()) {
			if (item.getType().equals(type)) {
				return item;
			}
		}
		return null;
	}
}
