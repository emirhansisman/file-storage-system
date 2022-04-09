import React, { useEffect, useState } from "react";
import UploadService from "../service/FileService";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faDownload,
  faFileDownload,
  faTrash,
} from "@fortawesome/free-solid-svg-icons";

const FileUpload = () => {
  const [selectedFiles, setSelectedFiles] = useState(undefined);
  const [currentFile, setCurrentFile] = useState(undefined);
  const [progress, setProgress] = useState(0);
  const [message, setMessage] = useState("");
  const [fileInfos, setFileInfos] = useState([]);

  useEffect(() => {
    UploadService.getFiles().then((response) => {
      setFileInfos(response.data);
    });
  }, []);

  useEffect(() => {
    setMessage(undefined);
    setProgress(0);
  }, [selectedFiles]);

  const selectFile = (event) => {
    setSelectedFiles(event.target.files);
  };

  const upload = () => {
    let currentFile = selectedFiles[0];

    setProgress(0);
    setCurrentFile(currentFile);

    UploadService.upload(currentFile, (event) => {
      setProgress(Math.round((100 * event.loaded) / event.total));
    })
      .then((response) => {
        setMessage(response.data.message);
        return UploadService.getFiles();
      })
      .then((files) => {
        setFileInfos(files.data);
      })
      .catch(() => {
        setProgress(0);
        setMessage("Could not upload the file!");
        setCurrentFile(undefined);
      });

    setSelectedFiles(undefined);
  };

  const handleDownload = (file) => {
    UploadService.downloadFile(file).catch(() => {
      setMessage("Could not download the file!");
    });
  };

  const handleDelete = (fileName) => {
    UploadService.deleteFile(fileName)
      .then(() => {
        return UploadService.getFiles();
      })
      .then((files) => {
        setFileInfos(files.data);
      })
      .catch(() => {
        setMessage("Could not delete the file!");
      });
  };

  const formatBytes = (bytes, decimals = 2) => {
    if (bytes === 0) return "0 Bytes";

    const k = 1024;
    const dm = decimals < 0 ? 0 : decimals;
    const sizes = ["Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"];

    const i = Math.floor(Math.log(bytes) / Math.log(k));

    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + " " + sizes[i];
  };

  return (
    <div>
      {currentFile && (
        <div className="progress">
          <div
            className="progress-bar progress-bar-info progress-bar-striped"
            role="progressbar"
            aria-valuenow={progress}
            aria-valuemin="0"
            aria-valuemax="100"
            style={{ width: progress + "%" }}
          >
            {progress}%
          </div>
        </div>
      )}

      <label className="btn btn-default upload-zone">
        <input type="file" onChange={selectFile} />
      </label>

      <button
        className="btn btn-success upload-button"
        disabled={!selectedFiles}
        onClick={upload}
      >
        Upload
      </button>

      <div className="alert alert-light" role="alert">
        {message}
      </div>

      <div className="card">
        <div className="card-header font-weight-bold">List of Files</div>
        <ul className="list-group list-group-flush">
          {fileInfos &&
            fileInfos.map((file, index) => (
              <li className="list-group-item file-item" key={index}>
                <div className="file-detail--group">
                  <p>{file.name}</p>
                  <p className="file-item--size">{formatBytes(file.size)}</p>
                  <div>
                    <button
                      className="btn btn-outline-light btn-sm"
                      onClick={() => handleDownload(file)}
                    >
                      <FontAwesomeIcon
                        icon={faFileDownload}
                        size="md"
                        color="#4CB963"
                      />
                    </button>
                    <button
                      className="btn btn btn-outline-light btn-sm"
                      onClick={() => handleDelete(file.name)}
                    >
                      <FontAwesomeIcon
                        icon={faTrash}
                        size="md"
                        color="#D04C4C"
                      />
                    </button>
                  </div>
                </div>
              </li>
            ))}
        </ul>
      </div>
    </div>
  );
};

export default FileUpload;
