import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

import FileUpload from "./component/FileUpload";

function App() {
  return (
    <div className="container mt-5" style={{ width: "900px" }}>
      <div className="my-3">
        <h4>File Storage System</h4>
      </div>
      <FileUpload />
    </div>
  );
}

export default App;
