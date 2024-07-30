const apiUrl = 'http://localhost:8080/files'; // 서버의 API URL

function uploadFile() {
    const fileInput = document.getElementById('fileInput');
    const file = fileInput.files[0];
    if (!file) {
        alert('Please select a file.');
        return;
    }

    const formData = new FormData();
    formData.append('file', file);

    fetch(`${apiUrl}/upload`, {
        method: 'POST',
        body: formData
    })
    .then(response => response.text())
    .then(fileKey => {
        addFileToList(file.name, fileKey);
    })
    .catch(error => {
        console.error('Error uploading file:', error);
        alert('Failed to upload file.');
    });
}

function addFileToList(fileName, fileKey) {
    const fileList = document.getElementById('fileList');

    const fileItem = document.createElement('div');
    fileItem.classList.add('file-item');

    const fileIcon = document.createElement('span');
    fileIcon.classList.add('icon');
    fileIcon.textContent = '📁'; // 파일 아이콘 이모티콘

    const fileNameSpan = document.createElement('span');
    fileNameSpan.textContent = fileName;

    const downloadButton = document.createElement('button');
    downloadButton.textContent = 'Download 📥';
    downloadButton.onclick = () => downloadFile(fileKey, fileName);

    fileItem.appendChild(fileIcon);
    fileItem.appendChild(fileNameSpan);
    fileItem.appendChild(downloadButton);

    fileList.appendChild(fileItem);
}

function downloadFile(fileKey, fileName) {
    fetch(`${apiUrl}/download/${fileKey}`, {
        method: 'GET'
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok.');
        }
        return response.blob();
    })
    .then(blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = fileName;
        document.body.appendChild(a);
        a.click();
        a.remove();
        window.URL.revokeObjectURL(url);
    })
    .catch(error => {
        console.error('Error downloading file:', error);
        alert('Failed to download file.');
    });
}
