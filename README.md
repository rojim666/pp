# 充电桩管理系统

一个现代化的充电桩管理系统，采用前后端分离架构，前端使用Vue 3，后端使用Spring Boot。

## 项目特点

✅ **Apple UI设计风格** - 玻璃拟态、柔和阴影、渐变色彩  
✅ **前后端分离** - RESTful API + JWT认证  
✅ **响应式布局** - 适配桌面端和移动端  
✅ **数据可视化** - ECharts图表展示  
✅ **状态管理** - Pinia集中式状态管理  
✅ **权限控制** - 基于角色的访问控制  
✅ **实时监控** - 充电桩状态实时更新  

## 功能特性

- 📊 **充电桩概览** - 实时统计充电时间、电量、金额和状态
- ⚙️ **资费设置** - 峰谷平时段配置、电价管理、服务费设置
- 🔌 **设备状态** - 实时监控充电桩功率、电压、电流、温度
- 📈 **使用统计** - 充电量、时长、次数趋势分析
- 🏪 **充电站管理** - 充电站CRUD、位置管理、参数配置
- 📝 **充电记录** - 充电历史查询、费用明细、导出功能
- 👥 **用户管理** - 用户信息、角色权限、VIP会员管理
- 💰 **费用统计** - 收入统计、财务报表、趋势图表
- 🔍 **系统健康** - 服务状态监控、系统日志、性能指标

## 技术栈

### 前端
- **Vue 3.3.4** - 渐进式JavaScript框架
- **Vite 4.4.9** - 下一代前端构建工具
- **Vue Router 4.2.4** - 官方路由管理器
- **Pinia 2.3.1** - Vue状态管理
- **Axios 1.6.2** - HTTP客户端
- **ECharts 5.4.3** - 数据可视化
- **CSS Variables** - 主题定制

### 后端
- **Java 17** - 编程语言
- **Spring Boot 3.1.5** - 企业级应用框架
- **Spring Security** - 安全认证
- **MyBatis-Plus 3.5.4.1** - ORM框架
- **MySQL 8.0** - 关系型数据库
- **Redis** - 缓存数据库
- **JWT** - Token认证
- **Swagger** - API文档

## 🚀 快速启动

### 一键启动(推荐) ⚡

**Windows**: 双击运行 **`start-all.bat`**

```powershell
.\start-all.bat
```

**Linux/Mac**:
```bash
chmod +x start-all.sh
./start-all.sh
```

脚本会自动:
1. ✅ 检查并启动MySQL
2. ✅ 初始化数据库
3. ✅ 安装依赖
4. ✅ 启动后端(新窗口)
5. ✅ 启动前端(新窗口)
6. ✅ 打开浏览器

访问: http://localhost:3000  
默认账户: `admin` / `admin123`

**详细说明**: 查看 [START.md](START.md)

---

### 手动启动

#### 环境要求

- **Node.js** 16+
- **npm** 或 **pnpm**
- **Java** 17+
- **Maven** 3.6+
- **MySQL** 8.0+
- **Redis** 6.0+ (可选)

#### 后端启动

\`\`\`bash
# 1. 导入数据库
mysql -u root -p < backend/sql/schema.sql

# 2. 修改配置
# 编辑 backend/src/main/resources/application.yml
# 配置数据库和Redis连接信息

# 3. 启动后端
cd backend
mvn spring-boot:run
\`\`\`

后端默认运行在: http://localhost:8080/api  
API文档: http://localhost:8080/api/swagger-ui.html

#### 前端启动

\`\`\`bash
# 1. 安装依赖
npm install

# 2. 启动开发服务器
npm run dev

# 3. 构建生产版本
npm run build

# 4. 预览生产构建
npm run preview
\`\`\`

前端默认运行在: http://localhost:3000

### 默认账户

\`\`\`
用户名: admin
密码: admin123
\`\`\`

## 项目结构

\`\`\`
.
├── backend/                    # 后端项目
│   ├── src/main/
│   │   ├── java/com/charging/
│   │   │   ├── controller/    # 控制器层
│   │   │   ├── service/       # 服务层
│   │   │   ├── mapper/        # 数据访问层
│   │   │   ├── entity/        # 实体类
│   │   │   ├── dto/           # 数据传输对象
│   │   │   ├── config/        # 配置类
│   │   │   ├── security/      # 安全模块
│   │   │   └── util/          # 工具类
│   │   └── resources/
│   │       └── application.yml
│   ├── sql/
│   │   └── schema.sql         # 数据库脚本
│   ├── pom.xml
│   └── README.md
│
├── src/                        # 前端源码
│   ├── api/                    # API接口
│   │   ├── request.js         # Axios配置
│   │   └── index.js           # API定义
│   ├── assets/                 # 静态资源
│   ├── components/             # 组件
│   │   ├── Layout.vue         # 布局组件
│   │   └── Icons.vue          # SVG图标库
│   ├── router/                 # 路由配置
│   │   └── index.js
│   ├── stores/                 # Pinia状态管理
│   │   ├── index.js
│   │   ├── user.js
│   │   ├── station.js
│   │   ├── record.js
│   │   └── dashboard.js
│   ├── views/                  # 页面组件
│   │   ├── Dashboard.vue      # 仪表盘
│   │   ├── Settings.vue       # 费率设置
│   │   ├── Device.vue         # 设备状态
│   │   ├── Usage.vue          # 使用统计
│   │   ├── Charging.vue       # 充电站管理
│   │   ├── Records.vue        # 充电记录
│   │   ├── Status.vue         # 设备状态监控
│   │   ├── User.vue           # 用户管理
│   │   ├── Statistics.vue     # 费用统计
│   │   └── System.vue         # 系统健康
│   ├── types/                  # TypeScript类型定义
│   │   └── index.ts
│   ├── utils/                  # 工具函数
│   │   └── index.ts
│   ├── mock/                   # Mock数据(开发用)
│   │   ├── generator.ts
│   │   └── database.ts
│   ├── App.vue
│   ├── main.js
│   └── style.css
│
├── .env                        # 环境变量
├── .env.development            # 开发环境变量
├── .env.production             # 生产环境变量
├── index.html
├── package.json
├── vite.config.js
└── README.md
\`\`\`

## API接口

详细API文档请查看Swagger: http://localhost:8080/api/swagger-ui.html

### 认证
- `POST /auth/login` - 用户登录
- `POST /auth/register` - 用户注册

### 用户
- `GET /users` - 用户列表
- `GET /users/{id}` - 用户详情
- `PUT /users/{id}` - 更新用户
- `DELETE /users/{id}` - 删除用户

### 充电桩
- `GET /stations` - 充电桩列表
- `GET /stations/{id}` - 充电桩详情
- `POST /stations` - 创建充电桩
- `PUT /stations/{id}` - 更新充电桩
- `DELETE /stations/{id}` - 删除充电桩
- `PATCH /stations/{id}/status` - 更新状态

### 充电记录
- `GET /records` - 充电记录列表
- `GET /records/{id}` - 充电记录详情
- `POST /records/start` - 开始充电
- `POST /records/{id}/end` - 结束充电

### 仪表盘
- `GET /dashboard/stats` - 统计数据

## 开发指南

### 前端开发

#### 调用API

\`\`\`javascript
import { stationAPI } from '@/api'

const fetchStations = async () => {
  try {
    const data = await stationAPI.list({ page: 1, size: 10 })
    console.log(data)
  } catch (error) {
    console.error(error.message)
  }
}
\`\`\`

#### 使用Pinia Store

\`\`\`javascript
import { useStationStore } from '@/stores/station'

const stationStore = useStationStore()
await stationStore.fetchStations()
const stations = stationStore.stations
\`\`\`

### 后端开发

#### 异常处理

\`\`\`java
throw new BusinessException("错误信息");
\`\`\`

#### 权限控制

\`\`\`java
@PreAuthorize("hasRole('ADMIN')")
public Result<?> deleteUser(Long id) {
  // ...
}
\`\`\`

## 部署

### 前端部署

\`\`\`bash
npm run build
# 将dist目录部署到Nginx或其他静态服务器
\`\`\`

### 后端部署

\`\`\`bash
cd backend
mvn clean package
java -jar target/charging-station-backend.jar
\`\`\`

## 浏览器支持

- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

## License

MIT License
