let stompClient = null;
let currentRoomId = null;
let currentUserId = null;

      function connect() {
        currentUserId = document.getElementById('userId').value.trim();
        if (!currentUserId) {
          addNewMessage('Please enter a User ID', 'system');
          return;
        }

        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect(
          {},
          function () {
            addNewMessage('Connected to WebSocket', 'system');

            // Personal messages
            stompClient.subscribe('/user/' + currentUserId, function (message) {
              const { sender, content } = JSON.parse(message.body);
              addNewMessage(`[Private] ${sender}: ${content}`, 'received');
            });
          },
          function (error) {
            addNewMessage('STOMP error: ' + error, 'system');
            disconnect();
          }
        );
      }

      function disconnect() {
        if (stompClient) {
          stompClient.disconnect();
          stompClient = null;
          currentRoomId = null;
          addNewMessage('Disconnected from WebSocket', 'system');
        }
      }

      function subscribeToRoom() {
        if (!stompClient) {
          addNewMessage('Please connect first', 'system');
          return;
        }

        const roomId = document.getElementById('roomId').value.trim();
        if (!roomId) {
          addNewMessage('Please enter a Room ID', 'system');
          return;
        }

        currentRoomId = roomId;

        // Room messages
        stompClient.subscribe('/topic/chat/' + roomId, function (message) {
          const { sender, content, id} = JSON.parse(message.body);
          const msg = `${sender}: ${content}`;
          const type = sender === currentUserId ? 'sent' : 'received';
          addNewMessage(msg, type, id);
        });

        // Edited messages
        stompClient.subscribe('/topic/chat/' + roomId + '/edit', function (message) {
            const editedMessage = JSON.parse(message.body);
            updateChangedMessage(editedMessage)
        })

        stompClient.subscribe('/topic/chat/' + roomId + '/delete', function (message) {
            updateDeletedMessage(message.body)
        })

        // History
        stompClient.subscribe('/app/chat/' + roomId, function (message) {
          const messages = JSON.parse(message.body);
          addNewMessage(`Loaded ${messages.length} previous messages`, 'system');
          messages.forEach(({ sender, content, id }) => {
            const msg = `${sender}: ${content}`;
            const type = sender === currentUserId ? 'sent' : 'received';
            addNewMessage(msg, type, id);
          });
        });

        addNewMessage('Subscribed to room: ' + roomId, 'system');
      }

      function createRoom() {
        if (!stompClient) {
          addNewMessage('Please connect first', 'system');
          return;
        }

        const userId = document.getElementById('userId').value.trim();
        const receiverId = document.getElementById('receiverId').value.trim();

        if (!userId || !receiverId) {
          addNewMessage('Please enter both User ID and Receiver ID', 'system');
          return;
        }

        // Wait for room creation announcement
        stompClient.subscribe('/topic/rooms', function (message) {
          const { roomId } = JSON.parse(message.body);
          currentRoomId = roomId;
          document.getElementById('roomId').value = roomId;
          addNewMessage('Room created: ' + roomId, 'system');
          subscribeToRoom();
        });

        stompClient.send(`/app/chat/create/${userId}/${receiverId}`, {}, JSON.stringify({}));
      }

      function sendMessage() {
        if (!stompClient || !currentRoomId) {
          addNewMessage('Please join a room first', 'system');
          return;
        }

        const content = document.getElementById('messageContent').value.trim();
        if (!content) return;

        const receiverId = document.getElementById('receiverId').value.trim();

        stompClient.send(
          '/app/chat/' + currentRoomId,
          {},
          JSON.stringify({ sender: currentUserId, receiver: receiverId, content })
        );

        document.getElementById('messageContent').value = '';
      }

// Update the addNewMessage function to separate username and content in the HTML structure
function addNewMessage(text, type, id=null) {
  timestamp = new Date()
  const wrap = document.getElementById('messages');
  const div = document.createElement('div');
  div.className = `message ${type}`;
  
  // Extract sender and content if format is "sender: content"
  let sender = '';
  let content = text;
  
  if (text.includes(':') && type !== 'system') {
    const parts = text.split(':');
    sender = parts[0];
    content = parts.slice(1).join(':').trim();
  }
  
  let messageHTML = '';
  
  if (type === 'system') {
    // System message
    messageHTML = `<div class="chat-bubble system">${text}</div>`;
  } else if (id) {
    // Regular chat message with separate spans for sender and content
    messageHTML = `
      <div class="message-container">
        <div id=${id} class="chat-bubble">
          ${sender ? `<span class="sender">${sender}</span>` : ''}
          <span class="message-content">${content}</span>
        </div>
        ${type === 'sent' ? 
          `<div class="actions">
            <span onclick="editMessage('${id}')" class="edit">edit</span> · 
            <span onclick="deleteMessage('${id}')" class="delete">delete</span>
          </div>` : ''}
        <div class="timestamp">${formatTime(timestamp)}</div>
      </div>
    `;
  }
  
  div.innerHTML = messageHTML;
  wrap.appendChild(div);
  wrap.scrollTop = wrap.scrollHeight;
}

// Add the formatTime function to fix the ReferenceError
function formatTime(timestamp) {
  if (!(timestamp instanceof Date)) {
    try {
      timestamp = new Date(timestamp);
    } catch (e) {
      return 'now'; // Fallback if date parsing fails
    }
  }
  
  // Format as HH:MM
  return timestamp.getHours().toString().padStart(2, '0') + ':' + 
         timestamp.getMinutes().toString().padStart(2, '0');
}

// Update the editMessage function to only edit the content part
function editMessage(id) {
  const messageDiv = document.getElementById(id);
  const contentSpan = messageDiv.querySelector('.message-content');
  const currentContent = contentSpan.textContent.trim();
  
  // Simple implementation - replace with actual edit logic
  const newContent = prompt('Edit message:', currentContent);
  if (newContent && newContent !== currentContent) {
    contentSpan.textContent = newContent;
    
    if (stompClient && currentRoomId) {
      stompClient.send(
        '/app/chat/edit/' + currentRoomId,
        {},
        JSON.stringify({
          messageId: id,
          content: newContent,
          sender: currentUserId
        })
      );
    }
  }
}

function updateChangedMessage(editedMessage) {

    const messageId = editedMessage.id;
    const messageDiv = document.getElementById(messageId);
    const newContent = editedMessage.content;
    if (messageDiv) {
        const contentSpan = messageDiv.querySelector('.message-content');
        contentSpan.textContent = newContent;
    } 
}

// Update the delete function too for consistency
function deleteMessage(id) {
    const messageDiv = document.getElementById(id);

    if (messageDiv && confirm("Are you sure you want to delete this message?")) {
        const container = messageDiv.closest('.message-container');
        const actionsDiv = container.querySelector('.actions');
        actionsDiv.remove();

        const contentSpan = messageDiv.querySelector('.message-content');
        contentSpan.textContent = "[This message has been deleted]";
        messageDiv.removeAttribute("id");

    } 

    if (stompClient && currentRoomId) {
      stompClient.send(
        '/app/chat/delete/' + currentRoomId,
        {},
        JSON.stringify({
          messageId: id, 
        })
      );
    }
}


function updateDeletedMessage(messageId) {
    const messageDiv = document.getElementById(messageId);

    if (messageDiv) {
        const contentSpan = messageDiv.querySelector('.message-content');
        contentSpan.textContent = "[This message has been deleted]";
        messageDiv.removeAttribute("id");
    } 
}