import axios from "axios";

const upload = (file, onUploadProgress) => {
  let formData = new FormData();

  const config = {
    headers: { "Content-Type": "multipart/form-data" },
    onUploadProgress,
  };

  formData.append("file", file);

  return axios.post(
    "http://localhost:8080/api/v1/files/upload",
    formData,
    config
  );
};

const getFiles = () => {
  return axios.get("http://localhost:8080/api/v1/files/");
};

const deleteFile = (fileName) => {
  return axios.delete(`http://localhost:8080/api/v1/files/${fileName}`);
};

const downloadFile = (file) => {
  axios({
    url: `http://localhost:8080/api/v1/files/${file.name}/content`,
    headers: {
      "Content-Type": file.extension,
      "Content-Disposition": `attachment; filename="${file.name}"`,
    },
    method: "get",
    responseType: "blob",
  }).then((response) => {
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement("a");
    link.href = url;
    link.setAttribute("download", file.name);
    document.body.appendChild(link);
    link.click();
  });
};

export default {
  upload,
  getFiles,
  deleteFile,
  downloadFile,
};
