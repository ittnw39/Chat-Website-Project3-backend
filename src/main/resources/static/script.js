const apiUrl = 'http://localhost:8080/files'; // 서버의 API URL

document.addEventListener('DOMContentLoaded', () => {
    loadFileList();
    loadDownloadedFileList();
});

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
        saveUploadedFile(file.name, fileKey);
    })
    .catch(error => {
        console.error('Error uploading file:', error);
        alert('Failed to upload file.');
    });
}

function loadFileList() {
    fetch(`${apiUrl}/list`, {
            method: 'GET'
        })
        .then(response => response.json())
        .then(fileList => {
            localStorage.setItem('uploadedFiles', JSON.stringify(fileList)); // 서버에서 가져온 파일 목록을 로컬 스토리지에 저장
            fileList.forEach(fileKey => {
                const fileName = fileKey.substring(fileKey.indexOf('_') + 1);
                addFileToList(fileName, fileKey);
            });
        })
        .catch(error => {
            console.error('Error loading file list:', error);
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

        // 다운로드 목록에 파일 추가
        addDownloadedFileToList(fileName);
        saveDownloadedFile(fileName); // 파일 목록을 로컬 저장소에 저장
    })
    .catch(error => {
        console.error('Error downloading file:', error);
        alert('Failed to download file.');
    });
}

function addDownloadedFileToList(fileName) {
    const downloadedFileList = document.getElementById('downloadedFileList');

    const fileItem = document.createElement('div');
    fileItem.classList.add('file-item');

    const fileIcon = document.createElement('span');
    fileIcon.classList.add('icon');
    fileIcon.textContent = '📁'; // 파일 아이콘 이모티콘

    const fileNameSpan = document.createElement('span');
    fileNameSpan.textContent = fileName;

    fileItem.appendChild(fileIcon);
    fileItem.appendChild(fileNameSpan);

    downloadedFileList.appendChild(fileItem);
}

function loadDownloadedFileList() {
    const downloadedFiles = JSON.parse(localStorage.getItem('downloadedFiles')) || [];
    downloadedFiles.forEach(fileName => {
        addDownloadedFileToList(fileName);
    });
}

function saveUploadedFile(fileName, fileKey) {
    const uploadedFiles = JSON.parse(localStorage.getItem('uploadedFiles')) || [];
    if (!uploadedFiles.some(file => file.fileKey === fileKey)) {
        uploadedFiles.push({ fileName, fileKey });
        localStorage.setItem('uploadedFiles', JSON.stringify(uploadedFiles));
    }
}

function saveDownloadedFile(fileName) {
    const downloadedFiles = JSON.parse(localStorage.getItem('downloadedFiles')) || [];
    if (!downloadedFiles.includes(fileName)) {
        downloadedFiles.push(fileName);
        localStorage.setItem('downloadedFiles', JSON.stringify(downloadedFiles));
    }
}

