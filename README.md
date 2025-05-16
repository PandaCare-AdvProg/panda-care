# panda-care
This is our group assignment for Advanced Programming Even 2024-2025

| Diagram Type       | Image |
|--------------------|-------|
| **Context Diagram** | ![Context Diagram](https://github.com/user-attachments/assets/db252af0-c1c9-4283-ad4c-c313fc5d8f97) |
| **Container Diagram** | ![Container Diagram](https://github.com/user-attachments/assets/4cb460e0-a88f-434c-b6a1-91b400009d74) |
| **Deployment Diagram** | ![Deployment Diagram](https://github.com/user-attachments/assets/9640d173-cd5f-40cd-8623-b5a76016088b) |


## After Risk Analysis

| Diagram Type         | Image |
|----------------------|-------|
| **Risk Analysis**     | ![Risk Analysis](https://github.com/user-attachments/assets/10b07f03-6c7f-4310-a0bf-9147c63d16e2) |
| **Context Diagram**   | ![Context Diagram](https://github.com/user-attachments/assets/d5174433-0136-4db6-b09e-b4b5b4c1a2af) |
| **Container Diagram** | ![Container Diagram](https://github.com/user-attachments/assets/d138b673-8816-40f4-af87-53b97c2fddcc) |
| **Deployment Diagram**| ![Deployment Diagram](https://github.com/user-attachments/assets/a3066898-bd23-4d2b-a180-dc9888ea9537) |

Risk Storming was applied to proactively identify and address potential threats to the PandaCare system’s architecture. This technique enabled our team to collaboratively explore critical risks—such as user data exposure, weak authentication, unsecured APIs, and system scalability bottlenecks—early in the design phase. By visually analyzing the architecture using C4 diagrams, we pinpointed vulnerable areas and introduced effective mitigations.

For example, we introduced an Authentication and Authorization container leveraging OAuth2 and JWT to secure API access and protect user data. Additionally, we enhanced scalability by integrating an Auto-Scaling Group with a Load Balancer to ensure that multiple Spring Boot instances can handle increased traffic reliably. This structured risk-focused approach ensures our system is not only functional, but also secure, resilient, and scalable from the ground up.


## Individual
### Izzy
| Diagram Type         | Image |
|----------------------|-------|
| **Code Diagram Authentication**     | ![Code Diagram Authentication](https://github.com/user-attachments/assets/6388d09f-430d-4d98-8df4-a325e981e16c) |
| **Code Diagram Schedule**   | ![Code Diagram Schedule](https://github.com/user-attachments/assets/f8c2ea01-cd98-48a4-9952-1215b598205e)|
| **Code Diagram Consultation** | ![Code Diagram Consultation](https://github.com/user-attachments/assets/7c57efa8-0d90-49bd-9ea8-71c9f083f6d1)|
| **Component Diagram**| ![Deployment Diagram](https://github.com/user-attachments/assets/179719dc-c1ed-4883-b0fc-497173562af2)|

### Diyo
| Diagram Type         | Image |
|----------------------|-------|
| **Code Diagram Doctor**     | ![DoctorModule](https://github.com/user-attachments/assets/b3fac4cc-4bab-4407-b39a-600276ba166c)|
| **Component Diagram**| ![Component drawio (2)](https://github.com/user-attachments/assets/36e02684-2b87-4e71-9044-460aedad488f)|


### KD
| Diagram Type         | Image |
|----------------------|-------|
| **Code Diagram Profile** |  ![alt text](profile.png)  |
| **Code Diagram Chat**     |  ![alt text](chat.png)  |
| **Component Diagram**| ![alt text](codedia.png)  |