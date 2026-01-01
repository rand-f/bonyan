🏗️ Bnyan – Construction Project Management System
📌 System Description (الوصف العربي)

النظام يحتوي على نوعين أساسيين من المستخدمين: الزبون والمتخصص. الزبون هو شخص يمكنه شراء أو استئجار المباني، المعارض، الوحدات السكنية، أو الأراضي التي يعرضها زبون آخر. كما يمكن للزبون تسجيل أملاكه الخاصة، سواء كانت مباني كاملة، جزء منها مثل الشقق، أو أراضٍ، ليتمكن من بيعها أو عرضها للإيجار.

أهم خاصية للنظام هي المساعدة في أعمال البناء، حيث يمكن للمستخدم تسجيل أرض، وبعد التحقق والموافقة عليها، يمكنه بدء مشروع جديد. عند بدء المشروع، يقوم المستخدم بتسجيل وصف المشروع، تاريخ البداية، الميزانية، والمدة المتوقعة للانتهاء.

يحتوي النظام على خصائص ذكاء اصطناعي متقدمة، حيث يقوم أولاً بتحليل وصف المشروع لإنتاج تقدير للميزانية والمدة الزمنية المتوقعة للانتهاء. كما يمكنه توليد صورة توضيحية تتوقع الشكل النهائي للمشروع لمساعدة المستخدم على تصور النتيجة. بالإضافة إلى ذلك، يقوم الذكاء الاصطناعي تلقائيًا بملء طلبات الانضمام للمتخصصين، مثل المهندسين والمصممين، مع تحديد وصف الطلب والمدة المتوقعة للبدء والانتهاء لكل متخصص، لتسهيل عملية اختيار الفريق المناسب للمشروع.

من خلال المنصة، يستطيع الزبون الوصول إلى تقييمات المتخصصين وخبراتهم لاختيار الأنسب للعمل على مشروعه. كما يمكنه جدولة اجتماعات عبر Zoom معهم، ودفع مستحقاتهم مباشرة من خلال المنصة. إضافة إلى ذلك، تقوم المنصة بإرسال إشعارات ورسائل عبر البريد الإلكتروني عند وصول الطلبات أو عند قبولها أو رفضها، سواء كانت طلبات انضمام المتخصصين أو طلبات البيع، الشراء، والاستئجار الخاصة بالزبائن.

بفضل هذه الخصائص، توفر المنصة تجربة متكاملة لإدارة المشاريع العقارية وأعمال البناء، تجمع بين التقديرات الذكية، سهولة اختيار المتخصصين، متابعة الأعمال، وإتمام المعاملات المالية والإشعارات بكل شفافية وسلاسة.

🚀 Overview

Bnyan is a comprehensive construction project management platform supporting:

customers

specialists

project managers

administrators

It enables:

real-estate listing and management

end-to-end construction project management

AI-assisted estimation and image generation

online payments

meetings and communication

role-based secure access

This project was developed as part of Tuwaiq Academy Final Project.

👥 Credit: Mohammed Alrashedi – Rand Abalkhail – Asrar Fallatah

🔗 Important Links
Item	Link
Postman API Documentation	https://documenter.getpostman.com/view/50792493/2sBXVbJZNj

Figma Prototype	https://www.figma.com/proto/0mdh83LYYxN3GfbdImGXXi/Bnyan?page-id=227%3A1512&node-id=233-5998&p=f

Deployment Link	http://bnyan-env.eba-grwzrbdm.eu-central-1.elasticbeanstalk.com/

🔐 Roles in the System

👤 USER (customer)

🛠️ SPECIALIST

🧑‍💼 PROJECT_MANAGER

🛡️ ADMIN

Rand models:
Specialist
SpesialistRequest
Domain
Project
ProjectManager

📊 API Endpoints Summary

| Method | Path                                                  | Access                  | Description                         | Owner |
| ------ | ----------------------------------------------------- | ----------------------- | ----------------------------------- | ----- |
| POST   | /api/v1/specialist/register                           | Public                  | Register specialist                 | Rand  |
| DELETE | /api/v1/specialist/delete/{id}                        | ADMIN                   | Delete specialist                   | Rand  |
| PUT    | /api/v1/specialist/update/{id}                        | SPECIALIST              | Update specialist                   | Rand  |
| GET    | /api/v1/specialist/get/arch-eng                       | USER, SPECIALIST, ADMIN | Get architects                      | Rand  |
| GET    | /api/v1/specialist/get/civil-eng                      | USER, SPECIALIST, ADMIN | Get civil engineers                 | Rand  |
| GET    | /api/v1/specialist/get/designer                       | USER, SPECIALIST, ADMIN | Get designers                       | Rand  |
| GET    | /api/v1/specialist/get/elect-eng                      | USER, SPECIALIST, ADMIN | Get electrical engineers            | Rand  |
| GET    | /api/v1/specialist/get/gen-cont                       | USER, SPECIALIST, ADMIN | Get general contractors             | Rand  |
| GET    | /api/v1/specialist/get/project-manager                | USER, SPECIALIST, ADMIN | Get project managers                | Rand  |
| GET    | /api/v1/domain/get                                    | ADMIN                   | Get all domains                     | Rand  |
| GET    | /api/v1/domain/get-specialist                         | ADMIN                   | Get specialist domain               | Rand  |
| POST   | /api/v1/domain/add                                    | ADMIN                   | Add domain                          | Rand  |
| PUT    | /api/v1/domain/update/{id}                            | ADMIN                   | Update domain                       | Rand  |
| DELETE | /api/v1/domain/delete/{id}                            | ADMIN                   | Delete domain                       | Rand  |
| POST   | /api/v1/specialist/assign-domain/{id}                 | SPECIALIST              | Assign domain to a specialist       | Rand  |
| GET    | /api/v1/project/get                                   | ADMIN                   | Get all projects                    | Rand  |
| GET    | /api/v1/project/get-my-projects                       | USER                    | Get my projects                     | Rand  |
| POST   | /api/v1/project/add/{request_id}                      | USER                    | Add project                         | Rand  |
| GET    | /api/v1/project/budget/{project_id}                   | USER                    | AI budget prediction                | Rand  |
| GET    | /api/v1/project/time-prediction/{project_id}          | USER                    | AI time estimation                  | Rand  |
| POST   | /api/v1/project/generate-image/{project_id}           | USER                    | AI image generation                 | Rand  |
| PUT    | /api/v1/project/update/{project_id}                   | USER                    | Update project                      | Rand  |
| DELETE | /api/v1/project/delete/{project_id}                   | USER                    | Delete project                      | Rand  |
| GET    | /api/v1/project/working-on-project/{project_id}       | USER, ADMIN             | See working team                    | Rand  |
| POST   | /api/v1/build-request/add                             | USER                    | Add build request                   | Rand  |
| PUT    | /api/v1/build-request/approve/{id}                    | SPECIALIST, ADMIN       | Approve build request               | Rand  |
| PUT    | /api/v1/build-request/reject/{id}                     | SPECIALIST, ADMIN       | Reject build request                | Rand  |
| GET    | /api/v1/build-request/get                             | USER, ADMIN             | Get build requests                  | Rand  |


| HTTP Method | Full Path                                        | Access            | Description                       |
| ----------- | ------------------------------------------------ | ----------------- | --------------------------------- |
| GET         | `/api/v1/customer/get`                           | ADMIN             | Get all customers                 |
| POST        | `/api/v1/customer/register-customer`             | PUBLIC            | Register new customer             |
| GET         | `/api/v1/customer/get-by-id`                     | CUSTOMER          | Get logged-in customer profile    |
| POST        | `/api/v1/customer/ask-ai`                        | CUSTOMER          | Ask AI a question                 |
| GET         | `/api/v1/customer/get-properties`                | CUSTOMER          | Get customer properties           |
| GET         | `/api/v1/customer/on-going-projects`             | CUSTOMER          | Get customer ongoing projects     |
| GET         | `/api/v1/customer/completed-projects`            | CUSTOMER          | Get customer completed projects   |
| GET         | `/api/v1/specialist/get`                         | ADMIN             | Get all specialists               |
| POST        | `/api/v1/specialist/register`                    | PUBLIC            | Register specialist               |
| POST        | `/api/v1/specialist/assign-domain/{domain_id}`   | SPECIALIST        | Assign domain to specialist       |
| PUT         | `/api/v1/specialist/accept-request/{request_id}` | SPECIALIST        | Accept customer request           |
| PUT         | `/api/v1/specialist/reject-request/{request_id}` | SPECIALIST        | Reject customer request           |
| PUT         | `/api/v1/specialist/update/{spec_id}`            | ADMIN, SPECIALIST | Update specialist                 |
| DELETE      | `/api/v1/specialist/delete/{specialist_id}`      | ADMIN             | Delete specialist                 |
| GET         | `/api/v1/specialist/get/arch-eng`                | ALL USERS         | Get architectural engineers       |
| GET         | `/api/v1/specialist/get/civil-eng`               | ALL USERS         | Get civil engineers               |
| GET         | `/api/v1/specialist/get/designer`                | ALL USERS         | Get designers                     |
| GET         | `/api/v1/specialist/get/elect-eng`               | ALL USERS         | Get electrical engineers          |
| GET         | `/api/v1/specialist/get/gen-cont`                | ALL USERS         | Get general contractors           |
| GET         | `/api/v1/specialist/get/mech-eng`                | ALL USERS         | Get mechanical engineers          |
| GET         | `/api/v1/specialist/get/project-manager`         | ALL USERS         | Get project managers              |
| GET         | `/api/v1/specialist/requests`                    | SPECIALIST        | Get logged-in specialist requests |
| GET         | `/api/v1/specialist-request/get`                                   | ADMIN             | Get all specialist requests             |
| POST        | `/api/v1/specialist-request/add/{project_id}/{spec_id}`            | CUSTOMER          | Add specialist request                  |
| PUT         | `/api/v1/specialist-request/accept/{requestId}`                    | SPECIALIST        | Accept specialist request               |
| PUT         | `/api/v1/specialist-request/add-manager/{project_id}/{manager_id}` | CUSTOMER          | Add manager request                     |
| PUT         | `/api/v1/specialist-request/reject/{requestId}`                    | SPECIALIST        | Reject specialist request               |
| PUT         | `/api/v1/specialist-request/update/{requestId}`                    | CUSTOMER          | Update specialist request               |
| DELETE      | `/api/v1/specialist-request/delete/{requestId}`                    | CUSTOMER          | Delete specialist request               |



I contributed to the project by developing multiple AI-driven features, including conversational chat with AI, image generation, and predictive modules for estimating project budget and timeline. In addition to AI functionality, I worked extensively on designing and implementing complex entity relationships within the system. I also assisted in revising and improving the business logic of several endpoints originally implemented by teammates. Furthermore, I automated several system processes, such as status updates based on dates and automatic rejection of unanswered requests after seven days, implemented through a scheduler that runs periodic checks every minute.

I also helped in the system's UI, in the project creation pages.
I was responsible for the deployment of the system on AWS.
