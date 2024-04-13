export default {
  size2Str: (limit) => {
    let size = "";
    const KB = 1024;
    const MB = KB * 1024;
    const GB = MB * 1024;
    if (limit < 0.1 * KB) {
      size = limit.toFixed(2) + "B";
    } else if (limit < 0.1 * MB) {
      size = (limit / KB).toFixed(2) + "KB";
    } else if (limit < 0.1 * GB) {
      size = (limit / MB).toFixed(2) + "MB";
    } else {
      size = (limit / GB).toFixed(2) + "GB";
    }
    let sizeStr = size.toString();
    let index = sizeStr.indexOf(".");
    let dou = sizeStr.substring(index + 1, index + 3);
    if (dou === "00") {
      return sizeStr.substring(0, index) + sizeStr.substring(index + 3, index + 5);
    }
    return sizeStr;
  },
};
