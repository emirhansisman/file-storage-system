import logo from "./logo.svg";
import "./App.css";

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
