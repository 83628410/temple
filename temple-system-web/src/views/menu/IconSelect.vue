<template>
  <el-popover placement="bottom" :width="400" trigger="click">
    <template #reference>
      <el-input :model-value="modelValue" placeholder="请选择图标" readonly class="cursor-pointer">
        <template #prefix>
          <el-icon v-if="modelValue">
            <component :is="modelValue" />
          </el-icon>
          <span v-else class="text-gray-400">点击选择</span>
        </template>
      </el-input>
    </template>
    <div class="icon-select-popover">
      <div class="icon-grid">
        <div
          v-for="icon in iconList"
          :key="icon"
          class="icon-item"
          :class="{ active: modelValue === icon }"
          :title="icon"
          @click="handleSelect(icon)"
        >
          <el-icon size="18">
            <component :is="icon" />
          </el-icon>
        </div>
      </div>
    </div>
  </el-popover>
</template>

<script setup lang="ts">
// Element Plus 2.13.3 图标列表
const iconList = [
  // System
  'Plus', 'Minus', 'CirclePlus', 'Search', 'Female', 'Male', 'Aim', 'House',
  'FullScreen', 'Loading', 'Link', 'Service', 'Pointer', 'Star', 'Notification',
  'Connection', 'ChatDotRound', 'Setting', 'Clock', 'Position', 'Discount',
  'Odometer', 'ChatSquare', 'ChatRound', 'ChatLineRound', 'ChatLineSquare',
  'ChatDotSquare', 'View', 'Hide', 'Unlock', 'Lock', 'RefreshRight',
  'RefreshLeft', 'Refresh', 'Bell', 'MuteNotification', 'User', 'Check',
  'CircleCheck', 'Warning', 'CircleClose', 'Close', 'PieChart', 'More',
  'Compass', 'Filter', 'Switch', 'Select', 'SemiSelect', 'CloseBold',
  'EditPen', 'Edit', 'Message', 'MessageBox', 'TurnOff', 'Finished',
  'Delete', 'Crop', 'SwitchButton', 'Operation', 'Open', 'Remove',
  'ZoomOut', 'ZoomIn', 'InfoFilled', 'CircleCheckFilled', 'SuccessFilled',
  'WarningFilled', 'CircleCloseFilled', 'QuestionFilled', 'WarnTriangleFilled',
  'UserFilled', 'MoreFilled', 'Tools', 'HomeFilled', 'Menu', 'UploadFilled',
  'Avatar', 'HelpFilled', 'Share', 'StarFilled', 'Comment', 'Histogram',
  'Grid', 'Promotion', 'DeleteFilled', 'RemoveFilled', 'CirclePlusFilled',
  // Arrow
  'ArrowLeft', 'ArrowUp', 'ArrowRight', 'ArrowDown', 'ArrowLeftBold',
  'ArrowUpBold', 'ArrowRightBold', 'ArrowDownBold', 'DArrowRight', 'DArrowLeft',
  'Download', 'Upload', 'Top', 'Bottom', 'Back', 'Right', 'TopRight',
  'TopLeft', 'BottomRight', 'BottomLeft', 'Sort', 'SortUp', 'SortDown',
  'Rank', 'CaretLeft', 'CaretTop', 'CaretRight', 'CaretBottom', 'DCaret',
  'Expand', 'Fold',
  // Document
  'DocumentAdd', 'Document', 'Notebook', 'Tickets', 'Memo', 'Collection',
  'Postcard', 'ScaleToOriginal', 'SetUp', 'DocumentDelete', 'DocumentChecked',
  'DataBoard', 'DataAnalysis', 'CopyDocument', 'FolderChecked', 'Files',
  'Folder', 'FolderDelete', 'FolderRemove', 'FolderOpened', 'DocumentCopy',
  'DocumentRemove', 'FolderAdd', 'FirstAidKit', 'Reading', 'DataLine',
  'Management', 'Checked', 'Ticket', 'Failed', 'TrendCharts', 'List',
  // Media
  'Microphone', 'Mute', 'Mic', 'VideoPause', 'VideoCamera', 'VideoPlay',
  'Headset', 'Monitor', 'Film', 'Camera', 'Picture', 'PictureRounded',
  'Iphone', 'Cellphone', 'VideoCameraFilled', 'PictureFilled', 'Platform',
  'CameraFilled', 'BellFilled',
  // Traffic
  'Location', 'LocationInformation', 'DeleteLocation', 'Coordinate', 'Bicycle',
  'OfficeBuilding', 'School', 'Guide', 'AddLocation', 'MapLocation', 'Place',
  'LocationFilled', 'Van',
  // Food
  'Watermelon', 'Pear', 'NoSmoking', 'Smoking', 'Mug', 'GobletSquareFull',
  'GobletFull', 'KnifeFork', 'Sugar', 'Bowl', 'MilkTea', 'Lollipop',
  'Coffee', 'Chicken', 'Dish', 'IceTea', 'ColdDrink', 'CoffeeCup',
  'DishDot', 'IceDrink', 'IceCream', 'Dessert', 'IceCreamSquare',
  'ForkSpoon', 'IceCreamRound', 'Food', 'HotWater', 'Grape', 'Fries',
  'Apple', 'Burger', 'Goblet', 'GobletSquare', 'Orange', 'Cherry',
  // Items
  'Printer', 'Calendar', 'CreditCard', 'Box', 'Money', 'Refrigerator',
  'Cpu', 'Football', 'Brush', 'Suitcase', 'SuitcaseLine', 'Umbrella',
  'AlarmClock', 'Medal', 'GoldMedal', 'Present', 'Mouse', 'Watch',
  'QuartzWatch', 'Magnet', 'Help', 'Soccer', 'ToiletPaper', 'ReadingLamp',
  'Paperclip', 'MagicStick', 'Basketball', 'Baseball', 'Coin', 'Goods',
  'Sell', 'SoldOut', 'Key', 'ShoppingCart', 'ShoppingCartFull',
  'ShoppingTrolley', 'Phone', 'Scissor', 'Handbag', 'ShoppingBag',
  'Trophy', 'TrophyBase', 'Stopwatch', 'Timer', 'CollectionTag',
  'TakeawayBox', 'PriceTag', 'Wallet', 'Opportunity', 'PhoneFilled',
  'WalletFilled', 'GoodsFilled', 'Flag', 'BrushFilled', 'Briefcase', 'Stamp',
  // Weather
  'Sunrise', 'Sunny', 'Ship', 'MostlyCloudy', 'PartlyCloudy', 'Sunset',
  'Drizzling', 'Pouring', 'Cloudy', 'Moon', 'MoonNight', 'Lightning',
  // Other
  'ChromeFilled', 'Eleme', 'ElemeFilled', 'ElementPlus', 'Shop',
  'SwitchFilled', 'WindPower'
]

const props = defineProps<{
  modelValue: string
}>()

const emit = defineEmits<{
  'update:modelValue': [value: string]
}>()

const handleSelect = (icon: string) => {
  emit('update:modelValue', icon)
}
</script>

<style scoped>
.icon-select-popover {
  max-height: 300px;
  overflow-y: auto;
}

.icon-grid {
  display: grid;
  grid-template-columns: repeat(10, 1fr);
  gap: 4px;
}

.icon-item {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 6px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s;
}

.icon-item:hover {
  background-color: #f5f7fa;
}

.icon-item.active {
  background-color: #ecf5ff;
  color: #409eff;
}
</style>
